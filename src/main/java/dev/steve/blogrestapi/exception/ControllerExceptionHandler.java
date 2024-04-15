package dev.steve.blogrestapi.exception;

import dev.steve.blogrestapi.utility.CustomErrorResponse;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.*;

@RestControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<CustomErrorResponse<Object>> customExceptionHandler(
      CustomException exception,
      WebRequest request) {
    CustomErrorResponse<Object> errorResponse = new CustomErrorResponse<>(
        new Date(),
        exception.getStatus().value(),
        exception.getTitle(),
        exception.getMessage(),
        request.getDescription(false));

    return new ResponseEntity<>(errorResponse, exception.getStatus());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public CustomErrorResponse<Object> methodArgumentNotValidExceptionHandler(
      MethodArgumentNotValidException exception,
      WebRequest request) {
    Map<String, String> errors = new HashMap<>();
    exception
        .getBindingResult()
        .getAllErrors()
        .forEach(error -> {
          String fieldName = ((FieldError) error).getField();
          String errorMessage = error.getDefaultMessage();
          errors.put(fieldName, errorMessage);
        });

    return new CustomErrorResponse<>(
        new Date(),
        HttpStatus.BAD_REQUEST.value(),
        "Validation Error",
        errors,
        request.getDescription(false));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public CustomErrorResponse<Object> methodHttpMessageNotReadableExceptionHandler(
      HttpMessageNotReadableException exception,
      WebRequest request) {
    return new CustomErrorResponse<>(
        new Date(),
        HttpStatus.BAD_REQUEST.value(),
        "Malformed JSON Request",
        exception.getMessage(),
        request.getDescription(false));
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public CustomErrorResponse<Object> methodArgumentTypeMismatchExceptionHandler(
      MethodArgumentTypeMismatchException exception,
      WebRequest request) {
    return new CustomErrorResponse<>(
        new Date(),
        HttpStatus.BAD_REQUEST.value(),
        "Type Mismatch",
        exception.getMessage(),
        request.getDescription(false));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public CustomErrorResponse<Object> constraintViolationExceptionHandler(
      ConstraintViolationException exception,
      WebRequest request) {
    return new CustomErrorResponse<>(
        new Date(),
        HttpStatus.BAD_REQUEST.value(),
        "Constraint Violation",
        exception.getMessage(),
        request.getDescription(false));
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public CustomErrorResponse<Object> missingServletRequestParameterExceptionHandler(
      MissingServletRequestParameterException exception,
      WebRequest request) {
    return new CustomErrorResponse<>(
        new Date(),
        HttpStatus.BAD_REQUEST.value(),
        "Missing Parameters",
        exception.getParameterName() + " parameter is missing",
        request.getDescription(false));
  }

  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  @ResponseStatus(value = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
  public CustomErrorResponse<Object> httpMediaTypeNotSupportedExceptionHandler(
      HttpMediaTypeNotSupportedException exception,
      WebRequest request) {
    List<String> details = new ArrayList<>();

    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(exception.getContentType());
    stringBuilder.append(
        " media type is not supported. Supported media types are ");
    exception
        .getSupportedMediaTypes()
        .forEach(mediaType -> stringBuilder.append(mediaType).append(", "));

    details.add(stringBuilder.toString());

    return new CustomErrorResponse<>(
        new Date(),
        HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
        "Unsupported Media Type",
        details,
        request.getDescription(false));
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
  public CustomErrorResponse<Object> accessDeniedExceptionHandler(
      AccessDeniedException exception,
      WebRequest request) {
    return new CustomErrorResponse<>(
        new Date(),
        HttpStatus.UNAUTHORIZED.value(),
        "Unauthorized",
        exception.getMessage(),
        request.getDescription(false));
  }

  @ExceptionHandler(BadCredentialsException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public CustomErrorResponse<Object> badCredentialsExceptionHandler(
      BadCredentialsException exception,
      WebRequest request) {
    return new CustomErrorResponse<>(
        new Date(),
        HttpStatus.BAD_REQUEST.value(),
        "Bad Request",
        exception.getMessage(),
        request.getDescription(false));
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  public CustomErrorResponse<Object> globalExceptionHandler(
      Exception exception,
      WebRequest request) {
    return new CustomErrorResponse<>(
        new Date(),
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "Internal Server Error",
        exception.getMessage(),
        request.getDescription(false));
  }
}
