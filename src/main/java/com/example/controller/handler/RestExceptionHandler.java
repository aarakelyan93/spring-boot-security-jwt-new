package com.example.controller.handler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.example.dto.error.ApiError;
import com.example.exception.NonUniqueObjectException;
import com.example.exception.ObjectInvalidStateException;
import com.example.exception.ObjectNotFoundException;
import com.example.exception.UnsupportedOperationException;
import com.example.service.message.MessageSourceService;
import javax.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * The class for {@link ControllerAdvice @ControllerAdvice} classes
 * that wish to provide centralized exception handling across all
 * {@code @RequestMapping} methods through {@code @ExceptionHandler} methods.
 */
@Log4j2
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @Autowired
  private MessageSourceService messageSourceService;

  /**
   * Handles MethodArgumentNotValidException. Created to encapsulate errors with more detail
   *
   * @param ex      the MethodArgumentNotValidException exception
   * @param headers the Http Headers
   * @param status  the HTTP Status
   * @param request the web request object
   * @return the ApiError object
   */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                HttpHeaders headers,
                                                                HttpStatus status,
                                                                WebRequest request) {
    final var apiError = new ApiError(HttpStatus.BAD_REQUEST);
    apiError.setMessage("Method argument not valid");
    ex.getBindingResult().getFieldErrors().forEach(fe ->
        apiError.addValidationError(fe.getObjectName(), fe.getField(), fe.getRejectedValue(),
            fe.getDefaultMessage()));

    log.debug(apiError);

    return new ResponseEntity<>(apiError, apiError.getStatus());
  }

  /**
   * Handles MethodArgumentTypeMismatchException. Created to encapsulate errors with more detail
   *
   * @param ex the MethodArgumentTypeMismatchException exception
   * @return the ApiError object
   */
  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException ex) {
    log.warn(ex);
    ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
    apiError.setMessage(ex.getMessage());
    return new ResponseEntity<>(apiError, apiError.getStatus());
  }

  /**
   * Handles ConstraintViolationException. Created to encapsulate errors with more detail
   *
   * @param ex the ConstraintViolationException exception
   * @return the ApiError object
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex) {
    log.warn(ex);
    final var apiError = new ApiError(HttpStatus.BAD_REQUEST);
    apiError.setMessage("Validation error");
    apiError.addValidationErrors(ex.getConstraintViolations());
    return buildResponseEntity(apiError);
  }

  /**
   * Handles DataIntegrityViolationException. Created to encapsulate errors with more detail
   *
   * @param ex the DataIntegrityViolationException thrown when an attempt to insert or update
   *           data results in violation of an primary key or unique constraint
   * @return the ApiError object
   */
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiError> handleDataIntegrityViolationException(
      DataIntegrityViolationException ex) {
    log.error(ex);
    return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST,
        "Violation of an primary key or unique constraint. " + ex.getMessage(), ex));
  }

  @ExceptionHandler(NonUniqueObjectException.class)
  protected ResponseEntity<ApiError> handleDuplicatedEntityException(NonUniqueObjectException ex) {
    String message = messageSourceService.getMessage(ex);

    log.debug(message);

    var apiError = new ApiError(CONFLICT);
    apiError.setMessage(message);
    return buildResponseEntity(apiError);
  }

  @ExceptionHandler(ObjectInvalidStateException.class)
  protected ResponseEntity<ApiError> handleIllegalEntityStateException(
      final ObjectInvalidStateException ex) {
    final var message = messageSourceService.getMessage(ex);
    log.warn(ex.getMessage());
    final var apiError = new ApiError(BAD_REQUEST);
    apiError.setMessage(message);
    return buildResponseEntity(apiError);
  }

  @ExceptionHandler(UnsupportedOperationException.class)
  protected ResponseEntity<ApiError> handleIllegalEntityStateException(
      final UnsupportedOperationException ex) {
    final var message = messageSourceService.getMessage(ex);
    log.warn(ex.getMessage());
    final var apiError = new ApiError(CONFLICT);
    apiError.setMessage(message);
    return buildResponseEntity(apiError);
  }

  /**
   * The exception handles ObjectNotFoundException.
   *
   * @param ex the ObjectNotFoundException
   * @return the ApiError object
   */
  @ExceptionHandler(ObjectNotFoundException.class)
  protected ResponseEntity<ApiError> handleObjectNotFoundException(ObjectNotFoundException ex) {
    String message = messageSourceService.getMessage(ex);

    log.debug(message);

    var apiError = new ApiError(NOT_FOUND, message, ex);

    return buildResponseEntity(apiError);
  }


  private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
    return new ResponseEntity<>(apiError, apiError.getStatus());
  }

}
