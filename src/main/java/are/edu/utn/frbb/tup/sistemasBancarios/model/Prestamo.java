package are.edu.utn.frbb.tup.sistemasBancarios.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Prestamo {

    private static final AtomicLong idCounter = new AtomicLong();
    private long idPrestamo;
    private long numeroCliente;
    private double montoPrestamo;
    private TipoMoneda moneda;
    private int plazoEnMeses;
    private LocalDate fechaSolicitud;
    private final double tasaInteresAnual;
    private double montoCuota;
    private boolean estado;
    private List<Pagos> planPagos;

    public Prestamo(){
        this.idPrestamo = idCounter.incrementAndGet();
        this.fechaSolicitud = LocalDate.now();
        this.planPagos = new ArrayList<>();
        this.tasaInteresAnual = 5.0;
    }


    public long getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(long idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public List<Pagos> getPlanPagos() {
        return planPagos;
    }

    public void setPlanPagos(List<Pagos> planPagos) {
        this.planPagos = planPagos;
    }

    public long getNumeroCliente() {
        return numeroCliente;
    }

    public void setNumeroCliente(long numeroCliente) {
        this.numeroCliente = numeroCliente;
    }

    public double getMontoPrestamo() {
        return montoPrestamo;
    }

    public void setMontoPrestamo(double montoPrestamo) {
        this.montoPrestamo = montoPrestamo;
    }

    public TipoMoneda getMoneda() {
        return moneda;
    }

    public void setMoneda(TipoMoneda moneda) {
        this.moneda = moneda;
    }

    public int getPlazoEnMeses() {
        return plazoEnMeses;
    }

    public void setPlazoEnMeses(int plazoEnMeses) {
        this.plazoEnMeses = plazoEnMeses;
    }

    public LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDate fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public double getTasaInteresAnual() {
        return tasaInteresAnual;
    }


    public double getMontoCuota() {
        return montoCuota;
    }

    public void setMontoCuota(double montoCuota) {
        this.montoCuota = montoCuota;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Prestamo{" +
                "idPrestamo=" + idPrestamo +
                ", numeroCliente=" + numeroCliente +
                ", montoPrestamo=" + montoPrestamo +
                ", moneda=" + moneda +
                ", plazoEnMeses=" + plazoEnMeses +
                ", fechaSolicitud=" + fechaSolicitud +
                ", tasaInteresAnual=" + tasaInteresAnual +
                ", montoCuota=" + montoCuota +
                ", estado=" + estado +
                ", planPagos=" + planPagos +
                '}';
    }
}
