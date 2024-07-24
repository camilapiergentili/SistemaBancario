package are.edu.utn.frbb.tup.sistemasBancarios.persistence.entity;

import are.edu.utn.frbb.tup.sistemasBancarios.model.Pagos;
import are.edu.utn.frbb.tup.sistemasBancarios.model.Prestamo;
import are.edu.utn.frbb.tup.sistemasBancarios.model.TipoMoneda;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrestamoEntity extends BaseEntity {

    private final long numeroCliente;
    private final double montoPrestamo;
    private final String moneda;
    private final int plazoEnMeses;
    private final LocalDate fechaSolicitud;
    private final double tasaInteresAnual;
    private final double montoCuota;
    private final boolean estado;
    private final List<Pagos> descripcionPagos = new ArrayList<>();


    public PrestamoEntity(Prestamo prestamo) {
        super(prestamo.getIdPrestamo());
        this.numeroCliente = prestamo.getNumeroCliente();
        this.montoPrestamo = prestamo.getMontoPrestamo();
        this.moneda = prestamo.getMoneda() != null ? prestamo.getMoneda().getText() : null;
        this.plazoEnMeses = prestamo.getPlazoEnMeses();
        this.fechaSolicitud = prestamo.getFechaSolicitud();
        this.tasaInteresAnual = prestamo.getTasaInteresAnual();
        this.montoCuota = prestamo.getMontoCuota();
        this.estado = prestamo.isEstado();

        if (prestamo.getPlanPagos() != null && !prestamo.getPlanPagos().isEmpty()) {
            for (Pagos pago : prestamo.getPlanPagos()) {
                this.descripcionPagos.add(pago);
            }
        }
    }

    public long getNumeroCliente() {
        return numeroCliente;
    }

    public double getMontoPrestamo() {
        return montoPrestamo;
    }

    public String getMoneda() {
        return moneda;
    }

    public int getPlazoEnMeses() {
        return plazoEnMeses;
    }

    public LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }

    public double getTasaInteresAnual() {
        return tasaInteresAnual;
    }

    public double getMontoCuota() {
        return montoCuota;
    }

    public List<Pagos> getDescripcionPagos() {
        return descripcionPagos;
    }

    public Prestamo toPrestamo(){
        Prestamo prestamo = new Prestamo();
        prestamo.setIdPrestamo(super.getId());
        prestamo.setNumeroCliente(this.numeroCliente);
        prestamo.setMontoPrestamo(this.montoPrestamo);
        prestamo.setMoneda(TipoMoneda.fromString(this.moneda));
        prestamo.setPlazoEnMeses(this.plazoEnMeses);
        prestamo.setFechaSolicitud(this.fechaSolicitud);
        prestamo.setMontoCuota(this.montoCuota);
        prestamo.setEstado(this.estado);
        prestamo.setPlanPagos(this.descripcionPagos);

        return prestamo;

    }
}
