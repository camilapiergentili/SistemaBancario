package are.edu.utn.frbb.tup.sistemasBancarios.persistence;

import are.edu.utn.frbb.tup.sistemasBancarios.model.Movimientos;

import java.util.List;

public interface MovimientosDao{
    void save(Movimientos movimientos);
    List<Movimientos> findByNumeroCuenta(long numeroCuenta);
}
