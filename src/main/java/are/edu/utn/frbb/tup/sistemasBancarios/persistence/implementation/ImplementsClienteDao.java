package are.edu.utn.frbb.tup.sistemasBancarios.persistence.implementation;


import are.edu.utn.frbb.tup.sistemasBancarios.model.Cliente;
import are.edu.utn.frbb.tup.sistemasBancarios.model.Cuenta;
import are.edu.utn.frbb.tup.sistemasBancarios.persistence.AbstactDataBase;
import are.edu.utn.frbb.tup.sistemasBancarios.persistence.ClienteDao;
import are.edu.utn.frbb.tup.sistemasBancarios.persistence.entity.ClienteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;



@Repository
public class ImplementsClienteDao extends AbstactDataBase implements ClienteDao {

    @Autowired
    ImplementsCuentaDao cuentaDao;

    @Override
    protected String getEntityName() {
        return "CLIENTE";
    }

    @Override
    public void save(Cliente cliente){
        ClienteEntity clienteEntity = new ClienteEntity(cliente);
        getInMemoryDataBase().put(clienteEntity.getId(), clienteEntity);
    }

    @Override
    public Cliente find(long dni, boolean cargarCuentas){
        if(getInMemoryDataBase().get(dni) == null){
            return null;
        }
        Cliente cliente = ((ClienteEntity) getInMemoryDataBase().get(dni)).toCliente();
        if(cargarCuentas){
            for(Cuenta c : cuentaDao.cuentasDelCliente(dni)){

                cliente.setCuentasDelCliente(c);
            }
        }

        return cliente;
    }

    public void update(Cliente cliente){
        ClienteEntity clienteEntity = new ClienteEntity(cliente);
        getInMemoryDataBase().put(clienteEntity.getId(), clienteEntity);

    }

    public void deteleCliente(long dni){
        getInMemoryDataBase().remove(dni);
    }

}
