package are.edu.utn.frbb.tup.sistemasBancarios.model.exception;

public class CuentaAlreadyExistsException extends  Exception {
    public CuentaAlreadyExistsException(String message){
        super(message);
    }
}