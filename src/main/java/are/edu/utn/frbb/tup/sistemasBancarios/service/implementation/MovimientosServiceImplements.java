package are.edu.utn.frbb.tup.sistemasBancarios.service.implementation;

import are.edu.utn.frbb.tup.sistemasBancarios.model.Cliente;
import are.edu.utn.frbb.tup.sistemasBancarios.model.Cuenta;
import are.edu.utn.frbb.tup.sistemasBancarios.model.Movimientos;
import are.edu.utn.frbb.tup.sistemasBancarios.model.TipoTransaccion;
import are.edu.utn.frbb.tup.sistemasBancarios.model.exception.CantidadNegativaException;
import are.edu.utn.frbb.tup.sistemasBancarios.model.exception.ClienteNoExistsException;
import are.edu.utn.frbb.tup.sistemasBancarios.model.exception.CuentaNoEncontradaException;
import are.edu.utn.frbb.tup.sistemasBancarios.model.exception.NoAlcanzaException;
import are.edu.utn.frbb.tup.sistemasBancarios.persistence.implementation.ImplementsCuentaDao;
import are.edu.utn.frbb.tup.sistemasBancarios.persistence.implementation.ImplementsMovimientoDao;
import are.edu.utn.frbb.tup.sistemasBancarios.service.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovimientosServiceImplements implements MovimientoService {

    @Autowired
    ImplementsMovimientoDao movimientoDao;

    @Autowired
    ImplementsCuentaDao cuentaDao;

    @Autowired
    CuentaServiceImplementation cuentaService;

    @Autowired
    ClienteServiceImplementation clienteService;

    @Override
    public Cuenta verificarCuenta(long numeroCuenta) throws CuentaNoEncontradaException{
        Cuenta cuenta = cuentaDao.find(numeroCuenta, false);
        if(cuenta == null){
            throw new CuentaNoEncontradaException("La cuenta no se encontro, no se podra realizar el movimiento");

        }
        return cuenta;
    }


    @Override
    public void realizarMovimiento(Movimientos movimientos, Cuenta cuenta) throws CuentaNoEncontradaException {
        cuentaService.agregarMovimientosALaCuenta(movimientos, cuenta);
        movimientoDao.save(movimientos);
        cuentaDao.save(cuenta);
    }


    @Override
    public void retiro(long dniTitular, long numeroCuenta, double monto) throws CantidadNegativaException, NoAlcanzaException, CuentaNoEncontradaException, ClienteNoExistsException {
        Cuenta cuenta = verificarCuenta(numeroCuenta);
        Cliente cliente = clienteService.buscarClientePorDni(dniTitular);

        if(monto < 0){
            throw new CantidadNegativaException("La cantidad de debitar no puede ser negativa");
        }

        if(monto > cuenta.getSaldo()){
            throw new NoAlcanzaException("No tienen dinero suficiente para efectuar el retiro");

        }
        Movimientos retiro = new Movimientos();
        retiro.setTipoTransaccion(TipoTransaccion.RETIRO);
        retiro.setMonto(monto);
        retiro.setCuenta(cuenta);
        cuenta.setTitular(cliente);
        cuenta.setSaldo(cuenta.getSaldo() - monto);
        realizarMovimiento(retiro, cuenta);

    }

    @Override
    public void depositar(long dniTitular, long numeroCuenta, double monto) throws CantidadNegativaException, CuentaNoEncontradaException, ClienteNoExistsException {

        Cuenta cuenta = verificarCuenta(numeroCuenta);
        Cliente cliente = clienteService.buscarClientePorDni(dniTitular);

        if(monto < 0){
            throw new CantidadNegativaException("La cantidad a depositar no puede ser negativa");
        }

        Movimientos deposito = new Movimientos();
        deposito.setTipoTransaccion(TipoTransaccion.DEPOSITO);
        deposito.setMonto(monto);
        deposito.setCuenta(cuenta);

        cuenta.setTitular(cliente);
        cuenta.setSaldo(cuenta.getSaldo() + monto);

        realizarMovimiento(deposito, cuenta);

    }

    @Override
    public void transferencia(long cuentaOrigen, long cuentaDestino, double monto) throws CantidadNegativaException, NoAlcanzaException, CuentaNoEncontradaException, ClienteNoExistsException {
        Cuenta cuentaDelOrigen = verificarCuenta(cuentaOrigen);
        Cuenta cuentaDestinatario = verificarCuenta(cuentaDestino);

        retiro(cuentaDelOrigen.getTitular().getDni(),cuentaDelOrigen.getNumeroCuenta(), monto);
        depositar(cuentaDestinatario.getTitular().getDni(), cuentaDestinatario.getNumeroCuenta(), monto);

    }

    @Override
    public double consultaSaldo(long numeroCuenta) throws CuentaNoEncontradaException {
        Cuenta cuenta = verificarCuenta(numeroCuenta);
        return cuenta.getSaldo();
    }

}
