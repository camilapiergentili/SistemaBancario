package are.edu.utn.frbb.tup.sistemasBancarios.persistence;

import are.edu.utn.frbb.tup.sistemasBancarios.model.Cliente;


public interface ClienteDao {

    Cliente find(long dni, boolean cargarCuentas);
    void save(Cliente cliente);
}
