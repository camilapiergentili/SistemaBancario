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

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class TupResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Map<Class<? extends Exception>, Integer> exceptionToErrorCodeMap = new HashMap<>();

    static{
                // BAD_REQUEST
                exceptionToErrorCodeMap.put(UnderageException.class, 6000);
                exceptionToErrorCodeMap.put(NotFormatDateException.class, 6001);
                exceptionToErrorCodeMap.put(TipoCuentaNotSupportedException.class, 6002);
                exceptionToErrorCodeMap.put(IllegalArgumentException.class, 6003);

                // NOT_FOUND
                exceptionToErrorCodeMap.put(ClienteNoExistsException.class, 7001);
                exceptionToErrorCodeMap.put(ClienteNoCuentaTipoMonedaException.class, 7002);
                exceptionToErrorCodeMap.put(PrestamoNoExistsException.class, 7003);
                exceptionToErrorCodeMap.put(CuentaNoEncontradaException.class, 7004);

                // CONFLICT
                exceptionToErrorCodeMap.put(ClienteAlreadyExistsException.class, 8000);
                exceptionToErrorCodeMap.put(CuentaAlreadyExistsException.class, 8001);
                exceptionToErrorCodeMap.put(TipoCuentaAlreadyExistsException.class, 8002);

    }



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
            PrestamoNoExistsException.class,
            CuentaNoEncontradaException.class

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
        if (errorCode >= 6000 && errorCode < 7000) {
            return HttpStatus.BAD_REQUEST;
        } else if (errorCode >= 7000 && errorCode < 8000) {
            return HttpStatus.NOT_FOUND;
        } else if (errorCode >= 8000 && errorCode < 9000) {
            return HttpStatus.CONFLICT;
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }


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
