package com.example.tms.handler;

import com.example.tms.error.*;
import jakarta.validation.ConstraintViolationException;
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

    @ExceptionHandler(value = NoSuchUserException.class)
    public ResponseEntity<ErrorResponseBody> noSuchUserHandler(NoSuchUserException ex, WebRequest webRequest) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex, webRequest);
    }

    @ExceptionHandler(value = NoSuchAssigneeException.class)
    public ResponseEntity<ErrorResponseBody>noSuchContractorHandler(NoSuchAssigneeException ex, WebRequest webRequest) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex, webRequest);
    }

    @ExceptionHandler(value = IsNotAdminActionException.class)
    public ResponseEntity<ErrorResponseBody> isNotAdminHandler(IsNotAdminActionException ex, WebRequest webRequest) {
        return buildResponse(HttpStatus.FORBIDDEN, ex, webRequest);
    }

    @ExceptionHandler(value = NoSuchTaskException.class)
    public ResponseEntity<ErrorResponseBody> noSuchTaskHandler(NoSuchTaskException ex, WebRequest webRequest) {
        return buildResponse(HttpStatus.NOT_FOUND, ex, webRequest);
    }

    @ExceptionHandler(value = InvalidUserTaskException.class)
    public ResponseEntity<ErrorResponseBody> invalidUserTaskHandler(InvalidUserTaskException ex, WebRequest webRequest) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex, webRequest);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseBody> constraintViolationHandler(ConstraintViolationException ex, WebRequest webRequest) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex, webRequest);
    }

    @ExceptionHandler(value = NoSuchTaskMaintainer.class)
    public ResponseEntity<ErrorResponseBody> noSuchTaskMaintainerHandler(NoSuchTaskMaintainer ex, WebRequest webRequest) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex, webRequest);
    }

    private ResponseEntity<ErrorResponseBody> buildResponse(HttpStatus httpStatus, Exception ex, WebRequest webRequest) {
        ErrorResponseBody responseBody = new ErrorResponseBody(ex.getMessage(), webRequest.getDescription(false));
        return ResponseEntity.status(httpStatus).body(responseBody);
    }
}
