package are.edu.utn.frbb.tup.sistemasBancarios;

import are.edu.utn.frbb.tup.sistemasBancarios.controller.dto.ClienteDto;
import are.edu.utn.frbb.tup.sistemasBancarios.model.*;
import are.edu.utn.frbb.tup.sistemasBancarios.model.exception.*;
import are.edu.utn.frbb.tup.sistemasBancarios.persistence.implementation.ImplementsClienteDao;
import are.edu.utn.frbb.tup.sistemasBancarios.service.implementation.ClienteServiceImplementation;
import are.edu.utn.frbb.tup.sistemasBancarios.service.implementation.CuentaServiceImplementation;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class ClienteServiceTest {

    @Mock
    ImplementsClienteDao clienteDao;

    @InjectMocks
    ClienteServiceImplementation clienteService;

    @Mock
    CuentaServiceImplementation cuentaService;

    private ClienteDto clienteDto;
    private Cliente cliente;
    private Cuenta cuenta;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);

        clienteDto = new ClienteDto();
        clienteDto.setDni(38944251);
        clienteDto.setNombre("Camila");
        clienteDto.setApellido("Piergentili");
        clienteDto.setFechaNacimiento(String.valueOf(LocalDate.of(1997, 5, 11)));
        clienteDto.setTipoPersona("F");

        cliente = new Cliente();
        cliente.setDni(12345678);
        cliente.setNombre("Santiago");
        cliente.setApellido("Pererino");
        cliente.setFechaNacimiento(LocalDate.parse("2020-05-11"));
        cliente.setTipoPersona(TipoPersona.fromString("J"));

        cuenta = new Cuenta();
        cuenta.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
        cuenta.setTipoMoneda(TipoMoneda.ARS);
        cuenta.setTitular(cliente);


    }

    //               DAR ALTA CUENTA test

    //Valida que se arroje la excepcion ClienteAlreadyExistsException
    @Test
    public void clienteAlreadyExistsDarAltaClienteTest() throws ClienteAlreadyExistsException {

        when(clienteDao.find(38944251, false)).thenReturn(new Cliente());
        assertThrows(ClienteAlreadyExistsException.class, () -> clienteService.darAltaCliente(clienteDto));

    }

    //valida que se arroje la excepcion cuando el cliente no es mayor de edad
    @Test
    public void underageExceptionDarAltaClienteTest(){

        clienteDto.setFechaNacimiento(String.valueOf(LocalDate.of(2020, 5, 11)));

        assertThrows(UnderageException.class, () -> clienteService.darAltaCliente(clienteDto));

    }

    //Valida que la fecha ingresada si es null, se arroje la exception
    @Test
    public void fechaNacimientoNull(){
        clienteDto.setFechaNacimiento(null);

        assertThrows(NullPointerException.class, () -> clienteService.darAltaCliente(clienteDto));
    }

    //valida que el cliente se de de alta, y se invoque aunque sea 1 vez el metodo save()
    @Test
    public void darAltaClienteTest() throws ClienteAlreadyExistsException, UnderageException {

        clienteService.darAltaCliente(clienteDto);

        verify(clienteDao, times(1)).save(any(Cliente.class));
    }

    //                                 BUSCAR CLIENTE BY DNI test

    //Valida que si el cliente es null, se arroje la exception ClienteNoExistsException
    @Test
    public void buscarClienteByDniNullTest(){
        cliente.setDni(12345);

        when(clienteDao.find(12345, true)).thenReturn(null);

        assertThrows(ClienteNoExistsException.class, () -> clienteService.buscarClientePorDni(12345));
    }


    //valida el metodo buscarClientePorDni() de ClienteService
    @Test
    public void clienteByDniExitosoTest() throws ClienteNoExistsException {

        when(clienteDao.find(38944251, true)).thenReturn(cliente);

        Cliente clienteComparacion = clienteService.buscarClientePorDni(38944251);

        assertNotNull(clienteComparacion);
        assertEquals(cliente, clienteComparacion);

    }


    //                               valido metodo updateCliente() de ClienteService
    @Test
    public void updateClienteNoExistsTest(){
        when(clienteDao.find(anyLong(), eq(true))).thenReturn(null);
        assertThrows(ClienteNoExistsException.class, () -> clienteService.updateCliente(any(ClienteDto.class), anyLong()));
    }

    @Test
    public void clienteMenorDeEdadTest() throws UnderageException, ClienteAlreadyExistsException {

        clienteDto.setFechaNacimiento(String.valueOf(LocalDate.of(2020, 5, 11)));

        Cliente clienteExistente = createClienteWithCuentas();

        when(clienteDao.find(38944251, true)).thenReturn(clienteExistente);

        assertThrows(UnderageException.class, () -> clienteService.updateCliente(clienteDto, 38944251));

    }

    @Test
    public void actualizarTitularCuentaExitosoTest() throws CuentaNoEncontradaException, ClienteNoExistsException, UnderageException {

        long dniAntiguo = 38944251;

        Cliente clienteExistente = createClienteWithCuentas();

        when(clienteDao.find(38944251, true)).thenReturn(clienteExistente);

        doNothing().when(cuentaService).actualizarTitularCuenta(any(Cliente.class), eq(dniAntiguo));

        clienteService.updateCliente(clienteDto, dniAntiguo);
        verify(clienteDao, times(1)).deteleCliente(dniAntiguo);
        verify(clienteDao, times(1)).save(any(Cliente.class));


    }

    @Test
    public void actualizarClienteSinCuentasTest() throws ClienteNoExistsException, CuentaNoEncontradaException, UnderageException {


        long dniAntiguo = 38944251;

        when(clienteDao.find(dniAntiguo, true)).thenReturn(cliente);
        clienteService.updateCliente(clienteDto, dniAntiguo);

        verify(cuentaService, never()).actualizarTitularCuenta(any(Cliente.class), eq(dniAntiguo));
        verify(clienteDao, times(1)).deteleCliente(dniAntiguo);
        verify(clienteDao, times(1)).save(any(Cliente.class));

    }

    //valida que cuando el cliente ya tenga cuenta del mismo tipo y moneda me arroje la exception TipoCuentaAlreadyExistsException
    @Test
    public void agregarCuentasAlClienteExceptionTest() throws ClienteNoExistsException {
        Cliente cliente = createClienteWithCuentas();
        when(clienteDao.find(12345678, true)).thenReturn(cliente);

        when(cuentaService.tieneCuentaDeTipoMoneda(12345678, TipoMoneda.ARS, TipoCuenta.CUENTA_CORRIENTE)).thenReturn(true);
        assertThrows(TipoCuentaAlreadyExistsException.class, () -> clienteService.agregarCuentasAlCliente(cuenta, 12345678));

    }

    //                         AGREGAR CUENTAS AL CLIENTE test

    //valida que el cliente se guarde correctamente
    @Test
    public void agregarCuentasAlClienteExitosoTest() throws ClienteNoExistsException, TipoCuentaAlreadyExistsException {
        Cliente cliente = createClienteWithCuentas();
        when(clienteDao.find(12345678, true)).thenReturn(cliente);

        clienteService.agregarCuentasAlCliente(cuenta, cliente.getDni());

        verify(clienteDao, times(1)).save(cliente);
    }



    private Cliente createClienteWithCuentas() {
        cuenta.setTitular(cliente);
        cliente.setCuentasDelCliente(cuenta);

        return cliente;
    }






}
