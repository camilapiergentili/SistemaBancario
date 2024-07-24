package are.edu.utn.frbb.tup.sistemasBancarios.model;

public class InfoPrestamo {
    private double montoTotal;
    private int plazoEnMeses;
    private int pagosRealizados;
    private double saldoRestante;

    public int getPlazoEnMeses() {
        return plazoEnMeses;
    }

    public void setPlazoEnMeses(int plazoEnMeses) {
        this.plazoEnMeses = plazoEnMeses;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public int getPagosRealizados() {
        return pagosRealizados;
    }

    public void setPagosRealizados(int pagosRealizados) {
        this.pagosRealizados = pagosRealizados;
    }

    public double getSaldoRestante() {
        return saldoRestante;
    }

    public void setSaldoRestante(double saldoRestante) {
        this.saldoRestante = saldoRestante;
    }
}
