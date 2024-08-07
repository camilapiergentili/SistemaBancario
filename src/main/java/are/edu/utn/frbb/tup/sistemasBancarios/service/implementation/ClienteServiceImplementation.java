package are.edu.utn.frbb.tup.sistemasBancarios.service.implementation;


import are.edu.utn.frbb.tup.sistemasBancarios.controller.dto.ClienteDto;
import are.edu.utn.frbb.tup.sistemasBancarios.model.Cliente;
import are.edu.utn.frbb.tup.sistemasBancarios.model.Cuenta;
import are.edu.utn.frbb.tup.sistemasBancarios.model.exception.*;
import are.edu.utn.frbb.tup.sistemasBancarios.persistence.implementation.ImplementsClienteDao;
import are.edu.utn.frbb.tup.sistemasBancarios.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ClienteServiceImplementation implements ClienteService {

    @Autowired
    ImplementsClienteDao clienteDao;

    @Autowired
    CuentaServiceImplementation cuentaService;

    @Override
    public void darAltaCliente(ClienteDto clienteDto) throws ClienteAlreadyExistsException, UnderageException {

        Cliente cliente = new Cliente(clienteDto);

        if(clienteDao.find(cliente.getDni(), false) != null){
            throw new ClienteAlreadyExistsException("El cliente con dni " + cliente.getDni() + " ya es usuario de nuestro banco");
        }

        if(cliente.getFechaNacimiento() == null){
            throw new NullPointerException("La fecha de nacimiento no puede ser nula");
        }

        if(cliente.getEdad() < 18){
            throw new UnderageException("El cliente no puede ser menor a 18 años");
        }

        clienteDao.save(cliente);

    }

    @Override
    public Cliente buscarClientePorDni(long dni) throws ClienteNoExistsException {

        Cliente cliente = clienteDao.find(dni, true);

        if(cliente == null){
            throw new ClienteNoExistsException("El cliente con dni " + dni + " no existe");
        }

        return cliente;
    }


    @Override
    public void updateCliente(ClienteDto clienteDto, long dniAntiguo) throws ClienteNoExistsException, UnderageException, CuentaNoEncontradaException {

        Cliente clienteExistente = buscarClientePorDni(dniAntiguo);

        if(clienteExistente == null){
            throw new ClienteNoExistsException("El cliente con el dni proporcionada no es cliente de nuestro banco");
        }

        Cliente clienteActualizado = new Cliente(clienteDto);

        if(clienteActualizado.getFechaNacimiento() == null){
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser nula");
        }

        if(clienteActualizado.getEdad() < 18) {
            throw new UnderageException("El cliente no puede ser menor a 18 años");
        }


        if (!clienteExistente.getCuentasDelCliente().isEmpty()) {
            cuentaService.actualizarTitularCuenta(clienteActualizado, dniAntiguo);
        }

        clienteDao.deteleCliente(dniAntiguo);
        clienteDao.save(clienteActualizado);

    }

    @Override
    public void agregarCuentasAlCliente(Cuenta cuenta, long dni) throws TipoCuentaAlreadyExistsException, ClienteNoExistsException {
        Cliente cliente = buscarClientePorDni(dni);


        if(cuentaService.tieneCuentaDeTipoMoneda(cliente.getDni(), cuenta.getTipoMoneda(), cuenta.getTipoCuenta())){
            throw new TipoCuentaAlreadyExistsException("El cliente ya tiene cuenta del tipo " + cuenta.getTipoCuenta() + " y tipo moneda " + cuenta.getTipoMoneda());
        }
        cuenta.setTitular(cliente);
        cliente.setCuentasDelCliente(cuenta);

        clienteDao.save(cliente);
    }

}
