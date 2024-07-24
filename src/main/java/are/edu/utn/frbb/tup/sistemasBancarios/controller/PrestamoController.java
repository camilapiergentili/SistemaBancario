package are.edu.utn.frbb.tup.sistemasBancarios.controller;

import are.edu.utn.frbb.tup.sistemasBancarios.controller.dto.PrestamoDto;
import are.edu.utn.frbb.tup.sistemasBancarios.controller.validations.ValidationInput;
import are.edu.utn.frbb.tup.sistemasBancarios.model.PrestamoResponseGet;
import are.edu.utn.frbb.tup.sistemasBancarios.model.PrestamoResponsePost;
import are.edu.utn.frbb.tup.sistemasBancarios.model.exception.*;
import are.edu.utn.frbb.tup.sistemasBancarios.service.implementation.PrestamoServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping("/prestamo")
public class PrestamoController {

    @Autowired
    PrestamoServiceImplementation prestamoService;

    @Autowired
    ValidationInput validacionInput;

    @PostMapping
    public PrestamoResponsePost sacarPrestamo(@RequestBody PrestamoDto prestamoDto, WebRequest request) throws ClienteNoExistsException, ClienteNoCuentaTipoMonedaException, TipoCuentaAlreadyExistsException, CantidadNegativaException, CuentaNoEncontradaException {
        validacionInput.validarInputPrestamo(prestamoDto);
        return prestamoService.solicitarPrestamo(prestamoDto);
    }

    @GetMapping("/{numeroCliente}")
    public PrestamoResponseGet detallePrestamo(@PathVariable long numeroCliente) throws ClienteNoExistsException, PrestamoNoExistsException {
        validacionInput.validarDni(numeroCliente);
        return prestamoService.prestamosByCliente(numeroCliente);
    }


    @DeleteMapping
    public ResponseEntity<Object> pagarCouta(@RequestBody PrestamoDto prestamoDto, WebRequest request) throws ClienteNoExistsException, PrestamoNoExistsException, SaldoInsuficienteException, ClienteNoCuentaTipoMonedaException, CantidadNegativaException, NoAlcanzaException, CuentaNoEncontradaException {
        validacionInput.validarInputPrestamo(prestamoDto);
        prestamoService.pagarCouta(prestamoDto);
        return ResponseEntity.ok("Se abono la couta correctamente");
    }
}
