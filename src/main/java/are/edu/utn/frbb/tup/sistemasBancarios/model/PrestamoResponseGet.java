package are.edu.utn.frbb.tup.sistemasBancarios.model;

import java.util.ArrayList;
import java.util.List;

public class PrestamoResponseGet {
    private long numeroCliente;
    private List<InfoPrestamo> prestamos = new ArrayList<>();

    public long getNumeroCliente() {
        return numeroCliente;
    }

    public void setNumeroCliente(long numeroCliente) {
        this.numeroCliente = numeroCliente;
    }

    public List<InfoPrestamo> getPrestamos() {
        return prestamos;
    }

    public void setPrestamos(List<InfoPrestamo> prestamos) {
        this.prestamos = prestamos;
    }
}
