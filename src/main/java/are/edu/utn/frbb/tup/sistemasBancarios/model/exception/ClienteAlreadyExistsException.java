package are.edu.utn.frbb.tup.sistemasBancarios.model.exception;

public class ClienteAlreadyExistsException extends Exception{
    public ClienteAlreadyExistsException(String message){
        super(message);

    }
}