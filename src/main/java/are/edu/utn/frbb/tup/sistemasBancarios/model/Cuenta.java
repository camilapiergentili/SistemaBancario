package are.edu.utn.frbb.tup.sistemasBancarios.model;


import com.fasterxml.jackson.annotation.JsonIgnore;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Cuenta {

    private  long numeroCuenta;
    private double saldo;
    private LocalDate fechaAltaCuenta;

    @JsonIgnore
    private Cliente titular;
    private TipoMoneda tipoMoneda;
    private TipoCuenta tipoCuenta;

    private List<Movimientos> movimientosCuenta;

    public Cuenta(){
        this.numeroCuenta = Math.abs(new Random().nextLong());
        this.saldo = 0;
        this.fechaAltaCuenta = LocalDate.now();
        this.movimientosCuenta = new ArrayList<>();

    }

    public List<Movimientos> getMovimientosCuenta() {
        return movimientosCuenta;
    }

    public void addMovimientos(Movimientos movimiento) {
        if (this.movimientosCuenta == null) {
            this.movimientosCuenta = new ArrayList<>();
        }
        this.movimientosCuenta.add(movimiento);
    }

    public long getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public LocalDate getFechaAltaCuenta() {
        return fechaAltaCuenta;
    }

    public void setFechaAltaCuenta(LocalDate fechaAltaCuenta) {
        this.fechaAltaCuenta = fechaAltaCuenta;
    }

    public Cliente getTitular() {
        return titular;
    }

    public void setTitular(Cliente titular) {
        this.titular = titular;
    }

    public TipoMoneda getTipoMoneda() {
        return tipoMoneda;
    }

    public void setTipoMoneda(TipoMoneda tipoMoneda) {
        this.tipoMoneda = tipoMoneda;
    }

    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }



}