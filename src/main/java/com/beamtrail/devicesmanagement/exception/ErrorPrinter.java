// fjmpaez
package com.beamtrail.devicesmanagement.exception;

import org.springframework.http.HttpStatus;

public interface ErrorPrinter {

    HttpStatus getHttpStatus();

    String getErrorCode();

    String getDescription();

    String getExternalErrorCode();

}
