//package org.huydd.bus_ticket_Ecommercial_platform.configurations;
//
//
//import org.huydd.bus_ticket_Ecommercial_platform.exceptions.AccessDeniedException;
//import org.huydd.bus_ticket_Ecommercial_platform.exceptions.IdNotFoundException;
//;
//import org.huydd.bus_ticket_Ecommercial_platform.exceptions.NoPermissionException;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//
//import org.springframework.http.HttpStatusCode;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@ControllerAdvice
//public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
//
//    @ExceptionHandler(value = {IdNotFoundException.class})
//    public ResponseEntity<Object> handleIdNotFoundException(RuntimeException exception, WebRequest request) {
//        Map<String, String> responseBody = new HashMap<>();
//        responseBody.put("message", exception.getMessage());
//        return handleExceptionInternal(exception, responseBody, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
//    }
//
//    @ExceptionHandler(value = {AccessDeniedException.class, NoPermissionException.class})
//    public ResponseEntity<Object> handleAccessDeniedException(RuntimeException exception, WebRequest request) {
//        Map<String, String> responseBody = new HashMap<>();
//        responseBody.put("message", exception.getMessage());
//        return handleExceptionInternal(exception, responseBody, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
//    }
//
//}
