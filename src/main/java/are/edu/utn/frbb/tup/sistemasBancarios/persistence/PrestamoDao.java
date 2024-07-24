package are.edu.utn.frbb.tup.sistemasBancarios.persistence;

import are.edu.utn.frbb.tup.sistemasBancarios.model.Prestamo;

import java.util.List;

public interface PrestamoDao {

    void save(Prestamo prestamo);
    List<Prestamo> findPrestamoByCliente(long numeroCliente);

}
