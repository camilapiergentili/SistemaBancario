package are.edu.utn.frbb.tup.sistemasBancarios.controller.dto;

public class PrestamoDto {

    private long numeroCliente;
    private int plazoEnMeses;
    private long montoPrestamo;
    private String moneda;

    public long getNumeroCliente() {
        return numeroCliente;
    }

    public void setNumeroCliente(long numeroCliente) {
        this.numeroCliente = numeroCliente;
    }

    public int getPlazoEnMeses() {
        return plazoEnMeses;
    }

    public void setPlazoEnMeses(int plazoEnMeses) {
        this.plazoEnMeses = plazoEnMeses;
    }

    public long getMontoPrestamo() {
        return montoPrestamo;
    }

    public void setMontoPrestamo(long montoPrestamo) {
        this.montoPrestamo = montoPrestamo;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }
}
