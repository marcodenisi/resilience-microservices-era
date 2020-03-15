package dev.marcodenisi.resilience.controller;

import dev.marcodenisi.resilience.exception.RemoteCallException;
import dev.marcodenisi.resilience.model.ErrorMessage;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResilientControllerAdvice {

    @ExceptionHandler(value = RemoteCallException.class)
    public ResponseEntity<ErrorMessage> handleException() {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(new ErrorMessage("External service temporarily unavailable"));
    }

    @ExceptionHandler(value = CallNotPermittedException.class)
    public ResponseEntity<ErrorMessage> handleCallNotPermitted() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(new ErrorMessage("Circuit currently open!"));
    }

}
