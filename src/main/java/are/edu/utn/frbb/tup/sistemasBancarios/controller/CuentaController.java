package are.edu.utn.frbb.tup.sistemasBancarios.controller;

import are.edu.utn.frbb.tup.sistemasBancarios.controller.dto.CuentaDto;
import are.edu.utn.frbb.tup.sistemasBancarios.controller.validations.ValidationInput;
import are.edu.utn.frbb.tup.sistemasBancarios.model.Cuenta;
import are.edu.utn.frbb.tup.sistemasBancarios.model.exception.*;
import are.edu.utn.frbb.tup.sistemasBancarios.service.implementation.CuentaServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@RestController
@RequestMapping("/cuenta")
public class CuentaController {

    @Autowired
    CuentaServiceImplementation cuentaService;

    @Autowired
    ValidationInput validationInput;

    @PostMapping("/agregar")
    public ResponseEntity<Object> addCuenta(@RequestBody CuentaDto cuentaDto, WebRequest request) throws TipoCuentaNotSupportedException, TipoCuentaAlreadyExistsException, CuentaAlreadyExistsException, ClienteNoExistsException {
        validationInput.validarDni(cuentaDto.getDniTitular());
        cuentaService.darAltaCuenta(cuentaDto, cuentaDto.getDniTitular());
        return ResponseEntity.status(HttpStatus.CREATED).body("Cuenta agregada con éxito");
    }

    @GetMapping("/all/{dniTitular}")
    public List<Cuenta> allCuentasByCliente(@PathVariable long dniTitular) throws CuentaNoEncontradaException {
        validationInput.validarDni(dniTitular);
        return cuentaService.listCuentasByCliente(dniTitular);

    }

    @GetMapping("/{nroCuenta}")
    public Cuenta buscarCuentaPorNumero(@PathVariable long nroCuenta) throws CuentaNoEncontradaException {
        validationInput.validarNumeroCuenta(nroCuenta);
        return cuentaService.buscarCuentaPorNumero(nroCuenta);
    }
}
