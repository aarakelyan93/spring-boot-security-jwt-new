package com.example.dto.error;

import com.example.util.DateUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
public class ApiError {

  private HttpStatus status;
  private Long timestamp;
  private String message;
  private String debugMessage;
  private List<ApiSubError> subErrors;

  private ApiError() {
    timestamp = DateUtils.getNow();
  }

  /**
   * Creates an object with HTTP status only.
   *
   * @param status The HTTP status
   */
  public ApiError(HttpStatus status) {
    this();
    this.status = status;
  }

  /**
   * Creates an object with HTTP status, response message and exception message.
   *
   * @param status  the HTTP Status
   * @param message The response message
   * @param ex      the exception
   */
  public ApiError(HttpStatus status, String message, Throwable ex) {
    this();
    this.status = status;
    this.message = message;
    this.debugMessage = ex.getLocalizedMessage();
  }

  private void addSubError(ApiSubError subError) {
    if (subErrors == null) {
      subErrors = new ArrayList<>();
    }
    subErrors.add(subError);
  }

  public void addValidationError(String object, String field, Object rejectedValue,
                                 String message) {
    addSubError(new ApiValidationError(object, field, rejectedValue, message));
  }

  /**
   * Utility method for adding error of ConstraintViolation. Usually when a @Validated validation
   * fails.
   *
   * @param cv the ConstraintViolation
   */
  private void addValidationError(ConstraintViolation<?> cv) {
    this.addValidationError(
        cv.getRootBeanClass().getSimpleName(), cv.getRootBeanClass().getSimpleName(),
        cv.getInvalidValue(),
        cv.getMessage());
  }

  public void addValidationErrors(Set<ConstraintViolation<?>> constraintViolations) {
    constraintViolations.forEach(this::addValidationError);
  }

  public interface ApiSubError {
  }

  @Data
  @EqualsAndHashCode(callSuper = false)
  @AllArgsConstructor
  public static class ApiValidationError implements ApiSubError {
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;
  }
}