package are.edu.utn.frbb.tup.sistemasBancarios.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;

public class Movimientos {

    private long idTransaccion;
    private LocalDate fechaMovimiento;
    private TipoTransaccion tipoTransaccion;
    private double monto;
    @JsonIgnore
    private Cuenta cuenta;
    private static int contador = 1;

    public Movimientos(){
        this.idTransaccion = contador++;
        this.fechaMovimiento = LocalDate.now();
    }

    public long getIdTransaccion() {
        return idTransaccion;
    }

    public LocalDate getFechaMovimiento() {
        return fechaMovimiento;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    public void setIdTransaccion(long idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public void setFechaMovimiento(LocalDate fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public TipoTransaccion getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(TipoTransaccion tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }
}
