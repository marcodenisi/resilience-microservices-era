package dev.marcodenisi.resilience.controller;

import dev.marcodenisi.resilience.exception.RemoteCallException;
import dev.marcodenisi.resilience.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResilientControllerAdvice {

    @ExceptionHandler(value = RemoteCallException.class)
    public ResponseEntity<ErrorMessage> handleException() {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ErrorMessage("External service temporarily unavailable"));
    }

}
