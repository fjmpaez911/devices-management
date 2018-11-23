// fjmpaez
package com.beamtrail.devicesmanagement.exception;

import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.SpanAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@RequestMapping(produces = "application/json")
public class DefaultExceptionHandler {

    private static final String INVALID_MISSING_REQUIRED_ATTRIBUTES = "invalid parameter: ";
    private static final String SERVICE_RETURNED_ERROR = "service returned error: {}";
    private static final int ERROR_CODE_LENGTH = 3;
    private static final String ERROR_FIELD_SEPERATOR = ", ";

    @Value("${service.identifier:000}")
    private String serviceIdentifier;

    private String INTERNAL_SERVER_ERROR;
    private String REQUEST_VALIDATION_ERROR;

    @Autowired
    private SpanAccessor spanAccessor;

    @PostConstruct
    public void init() {
        INTERNAL_SERVER_ERROR = serviceIdentifier + "000";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse validationExceptions(MethodArgumentNotValidException ex) {

        List<FieldError> constraintViolations = ex.getBindingResult().getFieldErrors();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(INVALID_MISSING_REQUIRED_ATTRIBUTES);

        for (FieldError constraintViolation : constraintViolations) {
            stringBuilder.append(constraintViolation.getField()).append(ERROR_FIELD_SEPERATOR);
        }

        stringBuilder.delete(stringBuilder.lastIndexOf(ERROR_FIELD_SEPERATOR),
                stringBuilder.length());

        ErrorResponse errorResponse = new ErrorResponse.ErrorResponseBuilder(
                spanAccessor.getCurrentSpan().traceIdString()).description(stringBuilder.toString())
                        .errorCode(REQUEST_VALIDATION_ERROR).build();

        log.error(SERVICE_RETURNED_ERROR, errorResponse, ex);

        return errorResponse;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse validationConstraintViolationException(ConstraintViolationException ex) {

        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(INVALID_MISSING_REQUIRED_ATTRIBUTES);

        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            stringBuilder.append(getFieldName(constraintViolation)).append(ERROR_FIELD_SEPERATOR);
        }

        stringBuilder.delete(stringBuilder.lastIndexOf(ERROR_FIELD_SEPERATOR),
                stringBuilder.length());

        ErrorResponse errorResponse = new ErrorResponse.ErrorResponseBuilder(
                spanAccessor.getCurrentSpan().traceIdString()).description(stringBuilder.toString())
                        .errorCode(REQUEST_VALIDATION_ERROR).build();

        log.error(SERVICE_RETURNED_ERROR, errorResponse, ex);

        return errorResponse;
    }

    @ExceptionHandler({ ServletRequestBindingException.class,
            HttpMessageNotReadableException.class })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse controllerExceptions(Exception ex) {

        ErrorResponse errorResponse = new ErrorResponse.ErrorResponseBuilder(
                spanAccessor.getCurrentSpan().traceIdString()).description(ex.getMessage())
                        .errorCode(REQUEST_VALIDATION_ERROR).build();

        log.error(SERVICE_RETURNED_ERROR, errorResponse, ex);

        return errorResponse;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse unhandledErrors(Exception ex) {

        ErrorResponse errorResponse = new ErrorResponse.ErrorResponseBuilder(
                spanAccessor.getCurrentSpan().traceIdString()).description(ex.getMessage())
                        .errorCode(INTERNAL_SERVER_ERROR).build();

        log.error(errorResponse.toString(), ex);
        return errorResponse;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> httpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex)
            throws HttpRequestMethodNotSupportedException {

        log.warn(ex.getMessage(), ex);

        throw ex;
    }

    @ExceptionHandler(DefinedErrorException.class)
    public ResponseEntity<ErrorResponse> handleErrorEnum(DefinedErrorException ex) {

        log.error(ex.getMessage(), ex);

        HttpStatus httpStatus = ex.getHttpStatus() != null ? ex.getHttpStatus()
                : HttpStatus.INTERNAL_SERVER_ERROR;

        if (ex.getErrorEnum() == null) {

            ErrorResponse errorResponse = new ErrorResponse.ErrorResponseBuilder(
                    spanAccessor.getCurrentSpan().traceIdString()).description(ex.getMessage())
                            .errorCode(INTERNAL_SERVER_ERROR).build();

            ResponseEntity<ErrorResponse> response = new ResponseEntity<>(errorResponse,
                    httpStatus);

            log.error(errorResponse.toString(), ex);

            return response;

        } else {

            ErrorResponse errorResponse = new ErrorResponse.ErrorResponseBuilder(
                    spanAccessor.getCurrentSpan().traceIdString()).description(ex.getDescription())
                            .errorCode(getFullErrorCode(ex.getErrorCode()))
                            .externalErrorCode(ex.getExternalErrorCode()).build();

            ResponseEntity<ErrorResponse> response = new ResponseEntity<>(errorResponse,
                    ex.getHttpStatus());

            log.error(errorResponse.toString(), ex);

            return response;
        }

    }

    private String getFieldName(ConstraintViolation<?> constraintViolation) {

        String fieldName = null;

        if (constraintViolation != null && constraintViolation.getPropertyPath() != null) {

            String[] results = constraintViolation.getPropertyPath().toString().split("\\.");

            if (results.length > 0) {
                fieldName = results[results.length - 1];
            } else {
                fieldName = constraintViolation.getPropertyPath().toString();
            }
        }

        return fieldName;
    }

    private String getFullErrorCode(String exceptionErrorCode) {

        String fullErrorCode = null;

        if (exceptionErrorCode != null && exceptionErrorCode.length() > ERROR_CODE_LENGTH) {
            fullErrorCode = exceptionErrorCode;
        } else {
            fullErrorCode = serviceIdentifier + exceptionErrorCode;
        }

        return fullErrorCode;

    }

}