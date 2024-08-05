package are.edu.utn.frbb.tup.sistemasBancarios.controllerTest;


import are.edu.utn.frbb.tup.sistemasBancarios.controller.ClienteController;
import are.edu.utn.frbb.tup.sistemasBancarios.controller.dto.ClienteDto;
import are.edu.utn.frbb.tup.sistemasBancarios.service.implementation.ClienteServiceImplementation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ClienteControllerTest.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;;

    @MockBean
    ClienteServiceImplementation clienteService;

    @MockBean
    ClienteController clienteController;

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void exceptionClienteYaExisteTest(){

        ClienteDto clienteDto = new ClienteDto();




    }

}
