package are.edu.utn.frbb.tup.sistemasBancarios;


import are.edu.utn.frbb.tup.sistemasBancarios.controller.dto.PrestamoDto;
import are.edu.utn.frbb.tup.sistemasBancarios.model.*;
import are.edu.utn.frbb.tup.sistemasBancarios.model.exception.*;
import are.edu.utn.frbb.tup.sistemasBancarios.persistence.implementation.ImplementsPrestamoDao;
import are.edu.utn.frbb.tup.sistemasBancarios.service.ScoreCrediticioService;
import are.edu.utn.frbb.tup.sistemasBancarios.service.implementation.ClienteServiceImplementation;
import are.edu.utn.frbb.tup.sistemasBancarios.service.implementation.CuentaServiceImplementation;
import are.edu.utn.frbb.tup.sistemasBancarios.service.implementation.MovimientosServiceImplements;
import are.edu.utn.frbb.tup.sistemasBancarios.service.implementation.PrestamoServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PrestamoServiceTest {

    @InjectMocks
    PrestamoServiceImplementation prestamoService;

    @Mock
    ImplementsPrestamoDao prestamoDao;

    @Mock
    ClienteServiceImplementation clienteService;

    @Mock
    CuentaServiceImplementation cuentaService;

    @Mock
    MovimientosServiceImplements movimientoService;

    @Mock
    private ScoreCrediticioService score;


    private Cuenta cuenta;
    private PrestamoDto prestamoDto;
    private Prestamo prestamo;
    private Cliente cliente;

    @BeforeEach
    public void setUo(){
        MockitoAnnotations.openMocks(this);

        prestamo = new Prestamo();
        prestamo.setNumeroCliente(38944251);
        prestamo.setMoneda(TipoMoneda.ARS);
        prestamo.setPlazoEnMeses(12);
        prestamo.setMontoPrestamo(5000);

        prestamoDto = new PrestamoDto();
        prestamoDto.setNumeroCliente(38944251);
        prestamoDto.setPlazoEnMeses(12);
        prestamoDto.setMontoPrestamo(5000);
        prestamoDto.setMoneda("pesos");

        double tasaInteresMensual = prestamo.getTasaInteresAnual() / 12 /100;
        double valorCouta = prestamo.getMontoPrestamo() * tasaInteresMensual / (1 - Math.pow(1 + tasaInteresMensual, -prestamo.getPlazoEnMeses()));

        prestamo.setMontoCuota(valorCouta);

        cliente = new Cliente();
        cliente.setDni(38944251);
        cliente.setNombre("Santiago");
        cliente.setApellido("Pererino");
        cliente.setFechaNacimiento(LocalDate.parse("1997-05-11"));
        cliente.setTipoPersona(TipoPersona.fromString("J"));

        cuenta = new Cuenta();
        cuenta.setTipoMoneda(TipoMoneda.ARS);
        cuenta.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);

        cuenta.setTitular(cliente);
        cliente.setCuentasDelCliente(cuenta);


    }


    //                                   SOLICITAR PRESTAMO EXCEPTIONS test

    //se lance la exception ClienteNoExistsException()
    @Test
    public void clienteNullSolicitarPrestamoTest() throws ClienteNoExistsException {

        when(clienteService.buscarClientePorDni(cliente.getDni())).thenReturn(null);
        assertThrows(ClienteNoExistsException.class,
                () -> prestamoService.solicitarPrestamo(prestamoDto));
    }

    //se lance la exception ClienteNoCuentaTipoMonedaException
    @Test
    public void cuentaNullSolicitarPrestamo() throws ClienteNoExistsException {

        when(clienteService.buscarClientePorDni(cliente.getDni())).thenReturn(cliente);
        when(cuentaService.obtenerCuentaParaPrestamo(cliente.getDni(), TipoMoneda.ARS))
                .thenReturn(null);
        assertThrows(ClienteNoCuentaTipoMonedaException.class,
                () -> prestamoService.solicitarPrestamo(prestamoDto));

    }

    //valido que el prestamo sea rechazado

    @Test
    public void scoreCrediticioFalseTest() throws ClienteNoExistsException, CantidadNegativaException, CuentaNoEncontradaException, TipoCuentaAlreadyExistsException, ClienteNoCuentaTipoMonedaException {

        cliente.setDni(38944252);
        prestamoDto.setNumeroCliente(38944252);

        when(clienteService.buscarClientePorDni(38944252)).thenReturn(cliente);
        when(cuentaService.obtenerCuentaParaPrestamo(38944252, TipoMoneda.ARS))
                .thenReturn(cuenta);

        when(score.scoreCrediticio(cliente.getDni())).thenReturn(false);

        PrestamoResponsePost respuesta = prestamoService.solicitarPrestamo(prestamoDto);

        assertEquals("RECHAZADO", respuesta.getEstado());
        assertEquals("No se pudo otorgar el prestamo", respuesta.getMensaje());

    }


    @Test
    public void solicitarPrestamoExitosoTest() throws ClienteNoExistsException,  CuentaNoEncontradaException, ClienteNoCuentaTipoMonedaException {

        when(clienteService.buscarClientePorDni(38944251)).thenReturn(cliente);
        when(cuentaService.obtenerCuentaParaPrestamo(38944251, TipoMoneda.ARS))
                .thenReturn(cuenta);
        when(score.scoreCrediticio(anyLong())).thenReturn(true);

        Pagos pagos = new Pagos(1, prestamo.getMontoCuota());
        List<Pagos> planPagos = new ArrayList<>();
        planPagos.add(pagos);

        List<Pagos> primeraCouta = new ArrayList<>();
        primeraCouta.add(planPagos.get(0));

        PrestamoResponsePost respuesta = prestamoService.solicitarPrestamo(prestamoDto);

        assertEquals("APROBADO", respuesta.getEstado());
        assertEquals("El monto: $" + prestamoDto.getMontoPrestamo()
                + " fue acreditado en su cuenta", respuesta.getMensaje());
        assertEquals(primeraCouta.get(0).getNumeroCouta(),
                respuesta.getPlanPagos().get(0).getNumeroCouta());
        assertEquals(primeraCouta.get(0).getMonto(),
                respuesta.getPlanPagos().get(0).getMonto());


        verify(movimientoService, times(1))
                .depositar(cuenta.getNumeroCuenta(), prestamoDto.getMontoPrestamo());
        verify(prestamoDao, times(1)).save(any(Prestamo.class));
    }

    //               PRESTAMOS BY CLIENTES test

    //se valida que se lance la exception PrestamoNoExistsException()
    @Test
    public void prestamosByClientesNoExitosoTest() throws ClienteNoExistsException{

        when(clienteService.buscarClientePorDni(cliente.getDni())).thenReturn(cliente);

        List<Prestamo> prestamos = new ArrayList<>();

        when(prestamoDao.findPrestamoByCliente(cliente.getDni())).thenReturn(prestamos);

        assertThrows(PrestamoNoExistsException.class,
                () -> prestamoService.prestamosByCliente(cliente.getDni()));

    }

    //valido que se retorne correctamente la lista de prestamos del cliente
    @Test
    public void prestamosByClienteExitosoTest() throws ClienteNoExistsException, CuentaNoEncontradaException, ClienteNoCuentaTipoMonedaException, PrestamoNoExistsException {
        when(clienteService.buscarClientePorDni(cliente.getDni())).thenReturn(cliente);

        List<Prestamo> listPrestamo = new ArrayList<>();
        listPrestamo.add(prestamo);

        when(prestamoDao.findPrestamoByCliente(cliente.getDni())).thenReturn(listPrestamo);

        PrestamoResponseGet responseGet = prestamoService.prestamosByCliente(cliente.getDni());

        assertNotNull(responseGet);
        assertEquals(cliente.getDni(), responseGet.getNumeroCliente());
        assertFalse(responseGet.getPrestamos().isEmpty());
        assertEquals(1, responseGet.getPrestamos().size());
        assertEquals(prestamo.getMontoPrestamo(), responseGet.getPrestamos()
                .get(0).getMontoTotal());
        assertEquals(prestamo.getPlazoEnMeses(), responseGet.getPrestamos()
                .get(0).getPlazoEnMeses());


    }

    //              PAGAR CUOTA test


    //se valida que se lance la exception PrestamoNoExistsException()
    @Test
    public void prestamoNoExistsExceptionPagarCuotaTest(){

        when(prestamoDao.findPrestamoByCliente(cliente.getDni())).thenReturn(new ArrayList<>());
        assertThrows(PrestamoNoExistsException.class, () -> prestamoService.pagarCouta(prestamoDto));
    }

    //se valida que no haya cuotas pendientes a pagar
    @Test
    public void noHayCuotasPendientesExceptionTest() throws SaldoInsuficienteException, PrestamoNoExistsException, CantidadNegativaException, NoAlcanzaException, ClienteNoExistsException, CuentaNoEncontradaException, ClienteNoCuentaTipoMonedaException {

        List<Prestamo> listPrestamo = new ArrayList<>();
        listPrestamo.add(prestamo);

        when(prestamoDao.findPrestamoByCliente(cliente.getDni())).thenReturn(listPrestamo);

        assertThrows(IllegalArgumentException.class, () -> prestamoService.pagarCouta(prestamoDto));

    }


    //se valida que se lance la exception SaldoInsuficienteException()
    @Test
    public void saldoInsuficienteSolicitarPrestamoTest() throws ClienteNoExistsException {

        List<Prestamo> listPrestamo = new ArrayList<>();
        listPrestamo.add(prestamo);
        when(prestamoDao.findPrestamoByCliente(cliente.getDni())).thenReturn(listPrestamo);

        List<Pagos> planPagos = new ArrayList<>();
        planPagos.add(new Pagos(1, prestamo.getMontoCuota()));
        prestamo.setPlanPagos(planPagos);

        when(cuentaService.obtenerCuentaParaPrestamo(cliente.getDni(), TipoMoneda.ARS))
                .thenReturn(cuenta);
        when(cuentaService.tieneSaldoDisponible(cuenta, prestamo.getMontoCuota()))
                .thenReturn(false);
        assertThrows(SaldoInsuficienteException.class,
                () -> prestamoService.pagarCouta(prestamoDto));

    }

    @Test
    public void pagarCuotaExitosoTest() throws NoAlcanzaException, CuentaNoEncontradaException, ClienteNoExistsException, ClienteNoCuentaTipoMonedaException, SaldoInsuficienteException, PrestamoNoExistsException, CantidadNegativaException {
        List<Prestamo> listPrestamo = new ArrayList<>();
        listPrestamo.add(prestamo);

        List<Pagos> planPagos = new ArrayList<>();
        planPagos.add(new Pagos(1, prestamo.getMontoCuota()));
        prestamo.setPlanPagos(planPagos);


        when(prestamoDao.findPrestamoByCliente(cliente.getDni())).thenReturn(listPrestamo);
        when(cuentaService.obtenerCuentaParaPrestamo(cliente.getDni(), TipoMoneda.ARS))
                .thenReturn(cuenta);
        when(cuentaService.tieneSaldoDisponible(cuenta, prestamo.getMontoCuota()))
                .thenReturn(true);
        doNothing().when(movimientoService).retiro(cuenta.getNumeroCuenta(),
                prestamo.getMontoCuota());

        prestamoService.pagarCouta(prestamoDto);

        verify(prestamoDao, times(1)).save(prestamo);

    }

    @Test
    public void listaInfoPrestamoTest(){

        List<Pagos> planPagos = new ArrayList<>();
        planPagos.add(new Pagos(1, prestamo.getMontoCuota()));
        prestamo.setPlanPagos(planPagos);


        InfoPrestamo infoPrestamo = new InfoPrestamo();

        infoPrestamo.setMontoTotal(prestamo.getMontoPrestamo());
        infoPrestamo.setPlazoEnMeses(prestamo.getPlazoEnMeses());
        int pagosRealizados = prestamo.getPlazoEnMeses() - prestamo.getPlanPagos().size();
        infoPrestamo.setPagosRealizados(pagosRealizados);
        double saldoPendiente = (prestamo.getMontoCuota()  -
                (prestamo.getMontoCuota() * pagosRealizados));
        infoPrestamo.setSaldoRestante(saldoPendiente);


        List<InfoPrestamo> prestamosByClientesMosk = new ArrayList<>();
        prestamosByClientesMosk.add(infoPrestamo);

        List<Prestamo> listPrestamo = new ArrayList<>();
        listPrestamo.add(prestamo);
        List<InfoPrestamo> prestamosCliente = prestamoService.infoPrestamo(listPrestamo);

        assertEquals(prestamosByClientesMosk.get(0).getMontoTotal(),
                prestamosCliente.get(0).getMontoTotal());
        assertEquals(prestamosByClientesMosk.get(0).getPlazoEnMeses(),
                prestamosCliente.get(0).getPlazoEnMeses());

        assertEquals(prestamosByClientesMosk.get(0).getSaldoRestante(),
                prestamosCliente.get(0).getSaldoRestante());
        assertEquals(prestamosByClientesMosk.get(0).getPagosRealizados(),
                prestamosCliente.get(0).getPagosRealizados());

    }
    
}
