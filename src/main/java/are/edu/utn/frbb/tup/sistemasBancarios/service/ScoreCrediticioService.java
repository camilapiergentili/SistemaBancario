package are.edu.utn.frbb.tup.sistemasBancarios.service;

import are.edu.utn.frbb.tup.sistemasBancarios.persistence.implementation.ImplementsPrestamoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScoreCrediticioService {

    @Autowired
    ImplementsPrestamoDao prestamoDao;

    public boolean scoreCrediticio(long numeroCliente){
        boolean darPrestamo = false;

        if(prestamoDao.findPrestamoByCliente(numeroCliente).size() < 2){
            darPrestamo = true;
        }

        return darPrestamo;
    }



}
