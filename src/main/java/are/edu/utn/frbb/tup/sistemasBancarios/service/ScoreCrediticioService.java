package are.edu.utn.frbb.tup.sistemasBancarios.service;

import are.edu.utn.frbb.tup.sistemasBancarios.persistence.implementation.ImplementsPrestamoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScoreCrediticioService {

    @Autowired
    ImplementsPrestamoDao prestamoDao;

    public boolean scoreCrediticio(long numeroCliente){
        return sizePrestamos(numeroCliente) && ultimoDigito(numeroCliente);
    }

    private boolean sizePrestamos(long numeroCliente){
        return (prestamoDao.findPrestamoByCliente(numeroCliente).size() < 2);
    }

    private boolean ultimoDigito(long numeroCliente){
        String nroClienteStr = String.valueOf(numeroCliente);
        return nroClienteStr.charAt(nroClienteStr.length() - 1) != '2';
    }



}
