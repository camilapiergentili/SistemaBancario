package are.edu.utn.frbb.tup.sistemasBancarios.controller.validations;

import are.edu.utn.frbb.tup.sistemasBancarios.controller.dto.ClienteDto;
import are.edu.utn.frbb.tup.sistemasBancarios.controller.dto.PrestamoDto;
import are.edu.utn.frbb.tup.sistemasBancarios.controller.exceptions.NotFormatDateException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class ValidationInput {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void validarInputCliente(ClienteDto clienteDto){

        validarDni(clienteDto.getDni());

        validarFechaNacimiento(clienteDto.getFechaNacimiento());

        validarString(clienteDto.getNombre(), "nombre", 3);
        validarString(clienteDto.getApellido(), "apellido", 3);
        validarString(clienteDto.getBanco(), "banco", 4);


    }

    public void validarInputPrestamo(PrestamoDto prestamoDto){

        validarDni(prestamoDto.getNumeroCliente());

        try{
            Double.parseDouble(String.valueOf(prestamoDto.getMontoPrestamo()));
            if(prestamoDto.getMontoPrestamo() <= 0){
                throw new IllegalArgumentException("El monto no puede ser negativo");
            }
        }catch (NumberFormatException ex){
            throw new IllegalArgumentException("Error, solo se deben ingresar caracteres numericos");
        }

        try{
            Integer.parseInt(String.valueOf(prestamoDto.getPlazoEnMeses()));
            if(prestamoDto.getPlazoEnMeses() < 1 || prestamoDto.getPlazoEnMeses() > 72){
                throw new IllegalArgumentException("El plazo en meses debe ser mayor a 0 y no puede superar los 72 meses");
            }
        }catch (NumberFormatException ex){
            throw new IllegalArgumentException("Error, solo se deben ingresar caracteres numericos");
        }


    }

    private void validarFechaNacimiento(String fechaNacimientoStr){
        try{
            LocalDate fechaNacimiento = LocalDate.parse(fechaNacimientoStr, DATE_FORMATTER);
            LocalDate fechaActual = LocalDate.now();

            if (fechaNacimiento.isAfter(fechaActual)) {
                throw new NotFormatDateException("La fecha de nacimiento no puede estar en el futuro");
            }


        }  catch (DateTimeParseException | NotFormatDateException e) {
            throw new IllegalArgumentException("Formato de fecha de nacimiento inválido. Debe ser yyyy-MM-dd");
        }
    }

    private void validarString(String texto, String campo, int longMinima){

        if(texto == null || texto.length() <= longMinima){
            throw new IllegalArgumentException("El campo " + campo + " no puede ser nulo y debe contener al menos " + longMinima + " caracteres");
        }

        if(!texto.matches("[a-zA-Z]+")){
            throw new IllegalArgumentException("El campo " + campo + "  no puede contener caracteres numericos");
        }

    }

    public void validarDni(long dni){

        String dniStr = String.valueOf(dni);

        if(dniStr.length() != 7 && dniStr.length() != 8){
            throw new IllegalArgumentException("El dni debe contener 7 u 8 caracteres");
        }

        if(!dniStr.matches("\\d+")){
            throw new IllegalArgumentException("El dni debe contener caracteres numericos");

        }
    }

}
