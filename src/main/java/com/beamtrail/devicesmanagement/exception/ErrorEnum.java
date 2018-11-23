package com.beamtrail.devicesmanagement.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorEnum implements ErrorPrinter {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "000", null, "internal server error"),

    TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "010", null, "requesting to {0} timed out"),

    NOT_FOUND(HttpStatus.NOT_FOUND, "002", null, "device not found"),

    ALREADY_BOOKED(HttpStatus.BAD_REQUEST, "003", null, "device already booked"),

    ALREADY_RETURNED(HttpStatus.BAD_REQUEST, "004", null, "device already returned"),

    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "005", null, "invalid parameter: {0}"),

    EXTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "001", "{0}", "external error: {1}");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String externalErrorCode;
    private final String description;
}
