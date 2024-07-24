package are.edu.utn.frbb.tup.sistemasBancarios.service;

import are.edu.utn.frbb.tup.sistemasBancarios.controller.dto.ClienteDto;
import are.edu.utn.frbb.tup.sistemasBancarios.model.Cliente;
import are.edu.utn.frbb.tup.sistemasBancarios.model.Cuenta;
import are.edu.utn.frbb.tup.sistemasBancarios.model.exception.*;

public interface ClienteService {

    void darAltaCliente(ClienteDto clienteDto) throws ClienteAlreadyExistsException, UnderageException;
    Cliente buscarClientePorDni(long dni) throws ClienteNoExistsException;
    void updateCliente(ClienteDto clienteDto, long dniAntiguo) throws ClienteNoExistsException, UnderageException, CuentaNoEncontradaException;
    void agregarCuentasAlCliente(Cuenta cuenta, long dni) throws TipoCuentaAlreadyExistsException, ClienteNoExistsException;
}
