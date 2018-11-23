// fjmpaez
package com.beamtrail.devicesmanagement.exception;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "error_code", "description", "correlation_id", "external_error_code" })
public class ErrorResponse {

    public ErrorResponse(String errorCode, String description, String correlationId,
            String externalErrorCode) {

        super();
        this.errorCode = errorCode;
        this.description = description;
        this.correlationId = correlationId;
        this.externalErrorCode = externalErrorCode;
    }

    @JsonProperty("error_code")
    private String errorCode;

    @JsonProperty("description")
    private String description;

    @JsonProperty("correlation_id")
    private String correlationId;

    @JsonProperty("external_error_code")
    private String externalErrorCode;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public static class ErrorResponseBuilder {

        private String errorCode;
        private String description;
        private String correlationId;
        private String externalErrorCode;

        public ErrorResponseBuilder(String spanId) {
            this.correlationId = spanId;
        }

        public ErrorResponseBuilder errorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public ErrorResponseBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ErrorResponseBuilder correlationId(String correlationId) {
            this.correlationId = correlationId;
            return this;
        }

        public ErrorResponseBuilder externalErrorCode(String externalErrorCode) {
            this.externalErrorCode = externalErrorCode;
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(errorCode, description, correlationId, externalErrorCode);
        }

    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
