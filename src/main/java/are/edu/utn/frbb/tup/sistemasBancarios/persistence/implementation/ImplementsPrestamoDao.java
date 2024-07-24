package are.edu.utn.frbb.tup.sistemasBancarios.persistence.implementation;

import are.edu.utn.frbb.tup.sistemasBancarios.model.Prestamo;
import are.edu.utn.frbb.tup.sistemasBancarios.persistence.AbstactDataBase;
import are.edu.utn.frbb.tup.sistemasBancarios.persistence.PrestamoDao;
import are.edu.utn.frbb.tup.sistemasBancarios.persistence.entity.PrestamoEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ImplementsPrestamoDao extends AbstactDataBase implements PrestamoDao {

    @Override
    protected String getEntityName() {
        return "PRESTAMO";
    }


    @Override
    public void save(Prestamo prestamo) {
        PrestamoEntity prestamoEntity = new PrestamoEntity(prestamo);
        getInMemoryDataBase().put(prestamoEntity.getId(), prestamoEntity);
    }


    public List<Prestamo> findPrestamoByCliente(long numeroCliente){

        List<Prestamo> prestamosDelCliente = new ArrayList<>();
        for (Object object : getInMemoryDataBase().values()){
            PrestamoEntity prestamoEntity = (PrestamoEntity) object;

            if(prestamoEntity.getNumeroCliente() == numeroCliente){
                prestamosDelCliente.add(prestamoEntity.toPrestamo());
            }

        }

        return prestamosDelCliente;

    }

    public Prestamo findById(long idPrestamo){

        if(getInMemoryDataBase().get(idPrestamo) == null){
            return null;
        }

        return ((PrestamoEntity) getInMemoryDataBase().get(idPrestamo)).toPrestamo();

    }

    public void delete(Prestamo prestamo){
        getInMemoryDataBase().remove(prestamo.getIdPrestamo());
    }
}
