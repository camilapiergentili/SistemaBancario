package are.edu.utn.frbb.tup.sistemasBancarios.service.implementation;


import are.edu.utn.frbb.tup.sistemasBancarios.controller.dto.PrestamoDto;
import are.edu.utn.frbb.tup.sistemasBancarios.model.*;
import are.edu.utn.frbb.tup.sistemasBancarios.model.exception.*;
import are.edu.utn.frbb.tup.sistemasBancarios.persistence.implementation.ImplementsPrestamoDao;
import are.edu.utn.frbb.tup.sistemasBancarios.service.PrestamoService;
import are.edu.utn.frbb.tup.sistemasBancarios.service.ScoreCrediticioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrestamoServiceImplementation implements PrestamoService {

    @Autowired
    ImplementsPrestamoDao prestamoDao;

    @Autowired
    ClienteServiceImplementation clienteService;

    @Autowired
    CuentaServiceImplementation cuentaService;

    @Autowired
    ScoreCrediticioService score;

    @Autowired
    MovimientosServiceImplements movimientosService;


    @Override
    public PrestamoResponsePost solicitarPrestamo(PrestamoDto prestamoDto) throws ClienteNoExistsException, ClienteNoCuentaTipoMonedaException, TipoCuentaAlreadyExistsException, CantidadNegativaException, CuentaNoEncontradaException {

        Prestamo prestamo = toPrestamo(prestamoDto);

        Cliente cliente = clienteService.buscarClientePorDni(prestamo.getNumeroCliente());
        Cuenta cuentaParaPrestamo = cuentaService.obtenerCuentaParaPrestamo(prestamo.getNumeroCliente(), prestamo.getMoneda());


        if(cuentaParaPrestamo == null){
            throw new ClienteNoCuentaTipoMonedaException("El cliente no tiene cuenta del tipo de moneda: " + prestamo.getMoneda());
        }

        if(!score.scoreCrediticio(cliente.getDni())){
            prestamo.setEstado(false);

        }else{
            //cuentaService.modificarSaldo(cliente, cuentaParaPrestamo, prestamo.getMontoPrestamo());
            prestamo.setEstado(true);
            simularPlanPago(prestamo);
            movimientosService.depositar(cliente.getDni(), cuentaParaPrestamo.getNumeroCuenta(), prestamo.getMontoPrestamo());
            prestamoDao.save(prestamo);

        }

        return respuesta(prestamo);

    }

    @Override
    public PrestamoResponseGet prestamosByCliente(long numeroCliente) throws ClienteNoExistsException, PrestamoNoExistsException {

        PrestamoResponseGet prestamoGetResponse = new PrestamoResponseGet();

        if(clienteService.buscarClientePorDni(numeroCliente) != null){

            List<Prestamo> prestamosByCliente = prestamoDao.findPrestamoByCliente(numeroCliente);

            if(prestamosByCliente.isEmpty()){
                throw new PrestamoNoExistsException("El cliente con dni " + numeroCliente + " no tiene prestamos solicitados");
            }

            prestamoGetResponse.setNumeroCliente(numeroCliente);
            prestamoGetResponse.setPrestamos(infoPrestamo(prestamosByCliente));

        }

        return prestamoGetResponse;

    }

    @Override
    public void pagarCouta(PrestamoDto prestamoDto) throws PrestamoNoExistsException, ClienteNoCuentaTipoMonedaException, SaldoInsuficienteException, CantidadNegativaException, NoAlcanzaException, CuentaNoEncontradaException, ClienteNoExistsException {
        List<Prestamo> prestamosDelCliente = prestamoDao.findPrestamoByCliente(prestamoDto.getNumeroCliente());
        Prestamo prestamoPagar = null;

        for(Prestamo p : prestamosDelCliente){
            if(p.getMontoPrestamo() == prestamoDto.getMontoPrestamo() && p.getPlazoEnMeses() == prestamoDto.getPlazoEnMeses() && p.getMoneda() == TipoMoneda.fromString(prestamoDto.getMoneda())){
                prestamoPagar = p;
                break;
            }
        }

        if (prestamoPagar == null) {
            throw new PrestamoNoExistsException("No se encontro prestamo relacionado al cliente nro " + prestamoDto.getNumeroCliente());
        }

        List<Pagos> planPagos = prestamoPagar.getPlanPagos();

        if (planPagos == null || planPagos.isEmpty()) {
            throw new IllegalArgumentException("No hay cuotas pendientes para pagar.");
        }

        Cuenta cuentaParaPrestamo = cuentaService.obtenerCuentaParaPrestamo(prestamoPagar.getNumeroCliente(), prestamoPagar.getMoneda());

        if (cuentaParaPrestamo == null) {
            throw new ClienteNoCuentaTipoMonedaException("El cliente no tiene cuenta del tipo de moneda: " + prestamoPagar.getMoneda());
        }

        double montoCuota = prestamoPagar.getMontoCuota();
        if (!cuentaService.tieneSaldoDisponible(cuentaParaPrestamo, montoCuota)) {
            throw new SaldoInsuficienteException("Saldo insuficiente para pagar la cuota.");
        }

        movimientosService.retiro(prestamoDto.getNumeroCliente(), cuentaParaPrestamo.getNumeroCuenta(), prestamoPagar.getMontoCuota());


        planPagos.remove(0);

        prestamoPagar.setPlanPagos(planPagos);
        prestamoDao.save(prestamoPagar);

    }

    private List<InfoPrestamo> infoPrestamo(List<Prestamo> prestamosByCliente){
        List<InfoPrestamo> listDetallePrestamo = new ArrayList<>();

        for(Prestamo p : prestamosByCliente){
            InfoPrestamo datosPrestamo = new InfoPrestamo();

            double saldoPagarConIntereses = p.getMontoCuota()*p.getPlazoEnMeses();
            int pagosRealizados = p.getPlazoEnMeses() - p.getPlanPagos().size();

            datosPrestamo.setMontoTotal(p.getMontoPrestamo());
            datosPrestamo.setPlazoEnMeses(p.getPlazoEnMeses());
            datosPrestamo.setPagosRealizados(pagosRealizados);
            datosPrestamo.setSaldoRestante(calcularSaldoAPagar(saldoPagarConIntereses, p.getMontoCuota(), pagosRealizados));

            listDetallePrestamo.add(datosPrestamo);

        }

        return listDetallePrestamo;

    }

    private double calcularSaldoAPagar(double montoTotal, double montoCuota, int pagosRealizados){
        double saldoRestante = montoTotal - montoCuota*pagosRealizados;
        if(saldoRestante <= 0){
            saldoRestante = 0;
        }

        return saldoRestante;
    }


    private Prestamo toPrestamo(PrestamoDto prestamoDto){
        Prestamo prestamo = new Prestamo();
        prestamo.setMontoPrestamo(prestamoDto.getMontoPrestamo());
        prestamo.setPlazoEnMeses(prestamoDto.getPlazoEnMeses());
        prestamo.setMoneda(prestamoDto.getMoneda() != null ? TipoMoneda.fromString(prestamoDto.getMoneda()) : null);
        prestamo.setNumeroCliente(prestamoDto.getNumeroCliente());
        prestamo.setMontoCuota(valorCoutaMensual(prestamoDto.getMontoPrestamo(), prestamo.getTasaInteresAnual(), prestamoDto.getPlazoEnMeses()));


        return prestamo;

    }

    private PrestamoResponsePost respuesta(Prestamo prestamo){
        PrestamoResponsePost prestamoResponsePost = new PrestamoResponsePost();
        if(prestamo.isEstado()){
            prestamoResponsePost.setEstado("APROBADO");
            prestamoResponsePost.setMensaje("El monto: $" + prestamo.getMontoPrestamo() + " fue acreditado en su cuenta");
            prestamoResponsePost.setPlanPagos(mostrarPlanPago(prestamo));

        }else{
            prestamoResponsePost.setEstado("RECHAZADO");
            prestamoResponsePost.setMensaje("No se pudo otorgar el prestamo");

        }

        return prestamoResponsePost;
    }

    private double valorCoutaMensual(double valorTotalPrestamo, double tasaInteresAnual, int plazoEnMeses){

        double tasaInteresMensual = tasaInteresAnual / 12 /100;
        return valorTotalPrestamo * tasaInteresMensual / (1 - Math.pow(1 + tasaInteresMensual, -plazoEnMeses));
    }

    private void simularPlanPago(Prestamo prestamo){

        List<Pagos> planPagos = new ArrayList<>();
        double valorCouta = valorCoutaMensual(prestamo.getMontoPrestamo(), prestamo.getTasaInteresAnual(), prestamo.getPlazoEnMeses());

        for(int i = 1; i <= prestamo.getPlazoEnMeses(); i++){
            planPagos.add(new Pagos(i, valorCouta));
        }

        prestamo.setPlanPagos(planPagos);

    }

    private List<Pagos> mostrarPlanPago(Prestamo prestamo){

        List<Pagos> planPagos = prestamo.getPlanPagos();
        List<Pagos> primeraCouta = new ArrayList<>();

        if(!planPagos.isEmpty()){
            primeraCouta.add(planPagos.get(0));
        }

        return primeraCouta;

    }


}
