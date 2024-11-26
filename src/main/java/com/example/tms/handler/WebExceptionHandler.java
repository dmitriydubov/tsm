package com.example.tms.handler;

import com.example.tms.error.EmptyFieldException;
import com.example.tms.error.NoSuchUserException;
import com.example.tms.error.RefreshTokenException;
import com.example.tms.error.UserAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class WebExceptionHandler {

    @ExceptionHandler(value = RefreshTokenException.class)
    public ResponseEntity<ErrorResponseBody> refreshTokenExceptionHandler(RefreshTokenException ex, WebRequest webRequest) {
        return buildResponse(HttpStatus.FORBIDDEN, ex, webRequest);
    }

    @ExceptionHandler(value = UserAlreadyExistException.class)
    public ResponseEntity<ErrorResponseBody> alreadyExistHandler(UserAlreadyExistException ex, WebRequest webRequest) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex, webRequest);
    }

    @ExceptionHandler(value = EmptyFieldException.class)
    public ResponseEntity<ErrorResponseBody> emptyFieldHandler(EmptyFieldException ex, WebRequest webRequest) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex, webRequest);
    }

    @ExceptionHandler(value = NoSuchUserException.class)
    public ResponseEntity<ErrorResponseBody> noSuchUserHandler(NoSuchUserException ex, WebRequest webRequest) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex, webRequest);
    }

    private ResponseEntity<ErrorResponseBody> buildResponse(HttpStatus httpStatus, Exception ex, WebRequest webRequest) {
        ErrorResponseBody responseBody = new ErrorResponseBody(ex.getMessage(), webRequest.getDescription(false));
        return ResponseEntity.status(httpStatus).body(responseBody);
    }
}
