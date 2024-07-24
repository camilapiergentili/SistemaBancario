package are.edu.utn.frbb.tup.sistemasBancarios.service;

import are.edu.utn.frbb.tup.sistemasBancarios.controller.dto.PrestamoDto;
import are.edu.utn.frbb.tup.sistemasBancarios.model.PrestamoResponseGet;
import are.edu.utn.frbb.tup.sistemasBancarios.model.PrestamoResponsePost;
import are.edu.utn.frbb.tup.sistemasBancarios.model.exception.*;

public interface PrestamoService {

    PrestamoResponsePost solicitarPrestamo(PrestamoDto prestamoDto) throws ClienteNoExistsException, ClienteNoCuentaTipoMonedaException, TipoCuentaAlreadyExistsException, CantidadNegativaException, CuentaNoEncontradaException;
    PrestamoResponseGet prestamosByCliente(long numeroCliente) throws ClienteNoExistsException, PrestamoNoExistsException;
    void pagarCouta(PrestamoDto prestamoDto) throws PrestamoNoExistsException, ClienteNoCuentaTipoMonedaException, SaldoInsuficienteException, CantidadNegativaException, NoAlcanzaException, CuentaNoEncontradaException, ClienteNoExistsException;
}
