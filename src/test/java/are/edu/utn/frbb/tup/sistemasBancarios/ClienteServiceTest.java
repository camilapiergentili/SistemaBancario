package are.edu.utn.frbb.tup.sistemasBancarios;

import are.edu.utn.frbb.tup.sistemasBancarios.controller.dto.ClienteDto;
import are.edu.utn.frbb.tup.sistemasBancarios.model.Cliente;
import are.edu.utn.frbb.tup.sistemasBancarios.model.TipoPersona;
import are.edu.utn.frbb.tup.sistemasBancarios.model.exception.ClienteAlreadyExistsException;
import are.edu.utn.frbb.tup.sistemasBancarios.model.exception.UnderageException;
import are.edu.utn.frbb.tup.sistemasBancarios.persistence.implementation.ImplementsClienteDao;
import are.edu.utn.frbb.tup.sistemasBancarios.service.implementation.ClienteServiceImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class ClienteServiceTest {

    @Mock
    ImplementsClienteDao clienteDao;

    @InjectMocks
    ClienteServiceImplementation clienteService;

    @Test
    public void clienteAlreadyExistsTest() throws ClienteAlreadyExistsException {
        ClienteDto cliente = new ClienteDto();
        cliente.setDni(38944251);
        cliente.setNombre("Camila");
        cliente.setApellido("Piergentili");
        cliente.setFechaNacimiento(LocalDate.of(1997, 05, 11));
        cliente.setTipoPersona(String.valueOf(TipoPersona.PERSONA_FISICA));


        when(clienteDao.find(38944251, false)).thenReturn(new Cliente());
        assertThrows(ClienteAlreadyExistsException.class, () -> clienteService.darAltaCliente(cliente));

    }

    @Test
    public void clienteMayorDe18AniosTest(){
        ClienteDto cliente = new ClienteDto();
        cliente.setFechaNacimiento(LocalDate.of(2020, 05, 11));
        assertThrows(IllegalArgumentException.class, () -> clienteService.darAltaCliente(cliente));

    }

    @Test
    public void clienteSuccessTest() throws ClienteAlreadyExistsException, UnderageException {

        ClienteDto cliente = new ClienteDto();
        cliente.setDni(38944251);
        cliente.setNombre("Camila");
        cliente.setApellido("Piergentili");
        cliente.setFechaNacimiento(LocalDate.of(1997, 05, 11));
        cliente.setTipoPersona(String.valueOf(TipoPersona.PERSONA_FISICA));

        clienteService.darAltaCliente(cliente);

        // verify(clienteDao, times(1)).save(cliente);


    }



}
