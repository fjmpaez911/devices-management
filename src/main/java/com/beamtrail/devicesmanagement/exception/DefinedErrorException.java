// fjmpaez
package com.beamtrail.devicesmanagement.exception;

import java.text.MessageFormat;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DefinedErrorException extends RuntimeException {

    private static final long serialVersionUID = 2079537223870829764L;

    private ErrorPrinter errorEnum;

    private HttpStatus httpStatus;
    private String description;
    private String externalErrorCode;
    private String errorCode;

    public DefinedErrorException(ErrorPrinter errorEnum) {

        super(errorEnum.getDescription());
        setErrorEnum(errorEnum);
    }

    public DefinedErrorException(ErrorPrinter errorEnum, HttpStatus httpStatus, String errorCode,
            String externalErrorCode, String description) {

        this(errorEnum, httpStatus, errorCode, externalErrorCode, description, null);
    }

    public DefinedErrorException(ErrorPrinter errorEnum, HttpStatus httpStatus, String errorCode,
            String externalErrorCode, String description, Throwable cause) {

        super(description, cause);

        setErrorEnum(errorEnum);
        setHttpStatus(httpStatus);
        setErrorCode(errorCode);
        setExternalErrorCode(externalErrorCode);
        setDescription(description);
    }

    public DefinedErrorException(String message) {

        super(message);
        setDescription(message);
    }

    public DefinedErrorException(Throwable cause) {

        super(cause);
        setHttpStatusCode(cause);
    }

    public DefinedErrorException(String message, Throwable cause) {

        super(message, cause);
        setHttpStatusCode(cause);
        setDescription(message);
    }

    public DefinedErrorException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {

        super(message, cause, enableSuppression, writableStackTrace);
        setHttpStatusCode(cause);
        setDescription(message);
    }

    public DefinedErrorException(String message, Throwable cause, ErrorPrinter errorEnum) {

        super(message, cause);
        setHttpStatusCode(cause);
        setErrorEnum(errorEnum);
        setDescription(message);
    }

    public void setErrorEnum(ErrorPrinter errorEnum) {

        this.errorEnum = errorEnum;

        if (errorEnum != null) {
            setHttpStatus(errorEnum.getHttpStatus());
            setErrorCode(errorEnum.getErrorCode());
            setExternalErrorCode(errorEnum.getExternalErrorCode());
            setDescription(errorEnum.getDescription());
        }
    }

    public DefinedErrorException(final ErrorPrinter type, Object... values) {

        this(type, type.getHttpStatus(), type.getErrorCode(),
                type.getExternalErrorCode() != null
                        ? MessageFormat.format(type.getExternalErrorCode(), values)
                        : null,
                type.getDescription() != null ? MessageFormat.format(type.getDescription(), values)
                        : null,
                values != null && values.length > 0
                        && values[values.length - 1] instanceof Throwable
                                ? (Throwable) values[values.length - 1]
                                : null);
    }

    private void setHttpStatusCode(Throwable cause) {

        if (cause instanceof HttpStatusCodeException) {

            HttpStatusCodeException restE = (HttpStatusCodeException) cause;
            setHttpStatus(restE.getStatusCode());
        }
    }
}
