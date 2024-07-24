package are.edu.utn.frbb.tup.sistemasBancarios.controller.handler;

import are.edu.utn.frbb.tup.sistemasBancarios.controller.exceptions.NotFormatDateException;
import are.edu.utn.frbb.tup.sistemasBancarios.model.exception.*;
import jakarta.annotation.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class TupResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Map<Class<? extends Exception>, Integer> exceptionToErrorCodeMap = Map.of(



            //BAD_REQUEST
            UnderageException.class, 6000,
            NotFormatDateException.class, 6001,
            TipoCuentaNotSupportedException.class, 6002,
            IllegalArgumentException.class, 6003,


            //NOT_FOUND
            ClienteNoExistsException.class, 7001,
            ClienteNoCuentaTipoMonedaException.class, 7002,
            PrestamoNoExistsException.class, 7003,

            //CONFLICT
            ClienteAlreadyExistsException.class, 8000,
            CuentaAlreadyExistsException.class, 8001,
            TipoCuentaAlreadyExistsException.class, 8002
    );

    @ExceptionHandler(value = {
            ClienteAlreadyExistsException.class,
            UnderageException.class,
            IllegalArgumentException.class,
            ClienteNoExistsException.class,
            NotFormatDateException.class,
            TipoCuentaNotSupportedException.class,
            CuentaAlreadyExistsException.class,
            TipoCuentaAlreadyExistsException.class,
            ClienteNoCuentaTipoMonedaException.class,
            PrestamoNoExistsException.class

    })
    protected ResponseEntity<Object> handleSpecificExceptions(Exception ex, WebRequest request) {
        int errorCode = exceptionToErrorCodeMap.getOrDefault(ex.getClass(), 0);
        HttpStatus status = determineHttpStatus(errorCode);

        CustomApiError error = new CustomApiError();
        error.setErrorMessage(ex.getMessage());
        error.setErrorCode(errorCode);

        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    // Determina el HttpStatus basado en el código de error
    private HttpStatus determineHttpStatus(int errorCode) {
        if (errorCode >= 9000 && errorCode < 10000) {
            return HttpStatus.BAD_REQUEST;
        } else if (errorCode >= 8000 && errorCode < 9000) {
            return HttpStatus.CONFLICT;
        } else {
            return HttpStatus.NOT_FOUND;
        }
    }



//    @ExceptionHandler(value = {
//            IllegalArgumentException.class,
//            ClienteNoExistsException.class
//    })
//    protected ResponseEntity<Object> handleConflic(Exception ex, WebRequest request){
//
//        CustomApiError error = new CustomApiError();
//        error.setErrorMessage(ex.getMessage());
//
//        int codeError = 0;
//
//        if(ex instanceof IllegalArgumentException){
//            codeError = 8000;
//        }
//
//        if(ex instanceof ClienteNoExistsException){
//            codeError = 8001;
//        }
//
//
//        error.setErrorCode(codeError);
//        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
//    }



    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request){

        if(body == null){
            CustomApiError error = new CustomApiError();
            error.setErrorMessage(ex.getMessage());
            body = error;

        }

        return new ResponseEntity(body, headers, status);
    }



}
