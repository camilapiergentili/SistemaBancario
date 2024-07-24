package are.edu.utn.frbb.tup.sistemasBancarios.service;

import are.edu.utn.frbb.tup.sistemasBancarios.controller.dto.CuentaDto;
import are.edu.utn.frbb.tup.sistemasBancarios.model.Cuenta;
import are.edu.utn.frbb.tup.sistemasBancarios.model.exception.*;

import java.util.List;

public interface CuentaService {
    void darAltaCuenta(CuentaDto cuentaDto, long dni) throws CuentaAlreadyExistsException, TipoCuentaNotSupportedException, TipoCuentaAlreadyExistsException, ClienteNoExistsException;
    List<Cuenta> listCuentasByCliente(long dniTitular) throws CuentaNoEncontradaException;
}
