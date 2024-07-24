package are.edu.utn.frbb.tup.sistemasBancarios.model;

import java.time.LocalDate;
import java.time.Period;

public class Persona {
    private long dni;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
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

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getEdad(){
        LocalDate fechaActual = LocalDate.now();
        return Period.between(this.fechaNacimiento, fechaActual).getYears();
    }
}

