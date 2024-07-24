package are.edu.utn.frbb.tup.sistemasBancarios.persistence;

import are.edu.utn.frbb.tup.sistemasBancarios.model.Cuenta;

import java.util.List;

public interface CuentaDao {

    Cuenta find(long id, boolean cargarMovimientos);
    void save(Cuenta cuenta);
    List<Cuenta> cuentasDelCliente(long dni);

}
