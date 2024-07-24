package are.edu.utn.frbb.tup.sistemasBancarios.controller.dto;


import java.time.LocalDate;
import java.time.Period;

public class PersonaDto {

    private long dni;
    private String nombre;
    private String apellido;
    private String fechaNacimiento;

    public long getDni() {
        return dni;
    }

    public void setDni(long dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getEdad(){
        LocalDate fechaActual = LocalDate.now();
        return Period.between(LocalDate.parse(fechaNacimiento), fechaActual).getYears();
    }
}
