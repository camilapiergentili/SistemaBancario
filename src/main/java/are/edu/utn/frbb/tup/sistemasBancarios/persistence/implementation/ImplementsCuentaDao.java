package are.edu.utn.frbb.tup.sistemasBancarios.persistence.implementation;

import are.edu.utn.frbb.tup.sistemasBancarios.model.Cuenta;
import are.edu.utn.frbb.tup.sistemasBancarios.model.Movimientos;
import are.edu.utn.frbb.tup.sistemasBancarios.persistence.AbstactDataBase;
import are.edu.utn.frbb.tup.sistemasBancarios.persistence.CuentaDao;
import are.edu.utn.frbb.tup.sistemasBancarios.persistence.entity.CuentaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.List;

@Repository
public class ImplementsCuentaDao extends AbstactDataBase implements CuentaDao {

    @Autowired
    ImplementsMovimientoDao movimientoDao;

    @Override
    protected String getEntityName() {
        return "CUENTA";
    }

    @Override
    public Cuenta find(long id, boolean cargarMovimientos) {
        if(getInMemoryDataBase().get(id) == null){
            return null;
        }

        Cuenta cuenta = ((CuentaEntity) getInMemoryDataBase().get(id)).toCuenta();

        if(cargarMovimientos){
            for (Movimientos m : movimientoDao.findByNumeroCuenta(cuenta.getNumeroCuenta())){
                cuenta.addMovimientos(m);
            }
        }

        return cuenta;
    }

    @Override
    public void save(Cuenta cuenta) {
        CuentaEntity cuentaEntity = new CuentaEntity(cuenta);
        getInMemoryDataBase().put(cuenta.getNumeroCuenta(), cuentaEntity);
    }

    @Override
    public List<Cuenta> cuentasDelCliente(long dni) {
        List<Cuenta> cuentasCliente = new ArrayList<>();

        for (Object object : getInMemoryDataBase().values()){

            CuentaEntity cuentaEntity = (CuentaEntity) object;

            if(cuentaEntity.getDniTitutar() == dni){
                cuentasCliente.add(cuentaEntity.toCuenta());
            }
        }

        return cuentasCliente;
    }

    public void update(Cuenta cuenta) {
        CuentaEntity cuentaEntity = new CuentaEntity(cuenta);
        getInMemoryDataBase().put(cuenta.getNumeroCuenta(), cuentaEntity);
    }



}
