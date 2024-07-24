package are.edu.utn.frbb.tup.sistemasBancarios.controller;

import are.edu.utn.frbb.tup.sistemasBancarios.controller.dto.ClienteDto;
import are.edu.utn.frbb.tup.sistemasBancarios.controller.validations.ValidationInput;
import are.edu.utn.frbb.tup.sistemasBancarios.model.Cliente;
import are.edu.utn.frbb.tup.sistemasBancarios.model.exception.ClienteAlreadyExistsException;
import are.edu.utn.frbb.tup.sistemasBancarios.model.exception.ClienteNoExistsException;
import are.edu.utn.frbb.tup.sistemasBancarios.model.exception.CuentaNoEncontradaException;
import are.edu.utn.frbb.tup.sistemasBancarios.model.exception.UnderageException;
import are.edu.utn.frbb.tup.sistemasBancarios.service.implementation.ClienteServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    ClienteServiceImplementation clienteService;

    @Autowired
    ValidationInput validationInput;


    @PostMapping("/alta")
    public ResponseEntity<Object> crearCliente(@RequestBody ClienteDto clienteDto, WebRequest request) throws ClienteAlreadyExistsException, UnderageException {
        validationInput.validarInputCliente(clienteDto);
        clienteService.darAltaCliente(clienteDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Cliente creado con éxito");
    }

    @GetMapping("/{Id}")
    public Cliente buscarClientePorDni(@PathVariable long Id, WebRequest request) throws ClienteNoExistsException, UnderageException {
        return clienteService.buscarClientePorDni(Id);
    }

    @PutMapping("/update/{dni}")
    public ResponseEntity<Object> updateCliente(@PathVariable long dni, @RequestBody ClienteDto clienteDto, WebRequest request) throws ClienteNoExistsException, UnderageException, CuentaNoEncontradaException {
        clienteService.updateCliente(clienteDto, dni);
        return  ResponseEntity.ok("Cliente modificado con exito");
    }

}
