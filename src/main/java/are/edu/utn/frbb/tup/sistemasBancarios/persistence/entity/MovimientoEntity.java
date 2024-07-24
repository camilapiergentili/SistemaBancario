package are.edu.utn.frbb.tup.sistemasBancarios.persistence.entity;


import are.edu.utn.frbb.tup.sistemasBancarios.model.Movimientos;
import are.edu.utn.frbb.tup.sistemasBancarios.model.TipoTransaccion;

import java.time.LocalDate;


public class MovimientoEntity extends BaseEntity {

    private String tipoTransaccion;
    private LocalDate fechaMovimiento;
    private long numeroCuenta;
    private double monto;


    public MovimientoEntity(Movimientos movimientos) {
        super(movimientos.getIdTransaccion());
        this.tipoTransaccion = movimientos.getTipoTransaccion() != null ? movimientos.getTipoTransaccion().getText() : null;
        this.fechaMovimiento = movimientos.getFechaMovimiento();
        this.numeroCuenta = movimientos.getCuenta().getNumeroCuenta();
        this.monto = movimientos.getMonto();
    }


    public Movimientos toMovimiento(){
        Movimientos movimientos = new Movimientos();

        movimientos.setIdTransaccion(super.getId());
        movimientos.setFechaMovimiento(this.fechaMovimiento);
        movimientos.setTipoTransaccion(TipoTransaccion.fromString(tipoTransaccion));
        movimientos.setMonto(this.monto);

        return movimientos;

    }

    public String getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(String tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public LocalDate getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(LocalDate fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public long getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }
}
