package are.edu.utn.frbb.tup.sistemasBancarios.model;

public class Pagos {

    private int numeroCouta;
    private double monto;

    public Pagos(int numeroCouta, double monto){
        this.numeroCouta = numeroCouta;
        this.monto = monto;
    }

    public int getNumeroCouta() {
        return numeroCouta;
    }

    public void setNumeroCouta(int numeroCouta) {
        this.numeroCouta = numeroCouta;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }
}
