package are.edu.utn.frbb.tup.sistemasBancarios.service;

import are.edu.utn.frbb.tup.sistemasBancarios.model.Cuenta;
import are.edu.utn.frbb.tup.sistemasBancarios.model.Movimientos;
import are.edu.utn.frbb.tup.sistemasBancarios.model.exception.CantidadNegativaException;
import are.edu.utn.frbb.tup.sistemasBancarios.model.exception.ClienteNoExistsException;
import are.edu.utn.frbb.tup.sistemasBancarios.model.exception.CuentaNoEncontradaException;
import are.edu.utn.frbb.tup.sistemasBancarios.model.exception.NoAlcanzaException;

import java.util.List;

public interface MovimientoService {
    Cuenta verificarCuenta(long numeroCuenta) throws CuentaNoEncontradaException;
    void realizarMovimiento(Movimientos movimientos, Cuenta cuenta) throws CuentaNoEncontradaException;
    public void retiro(long dniTitular, long numeroCuenta, double monto) throws CantidadNegativaException, NoAlcanzaException, CuentaNoEncontradaException, ClienteNoExistsException;
    public void depositar(long dniTitular, long numeroCuenta, double monto) throws CantidadNegativaException, CuentaNoEncontradaException, ClienteNoExistsException;
    void transferencia(long cuentaOrigen, long cuentaDestino, double monto) throws CantidadNegativaException, NoAlcanzaException, CuentaNoEncontradaException, ClienteNoExistsException;
    double consultaSaldo(long numeroCuenta) throws CuentaNoEncontradaException;

}
