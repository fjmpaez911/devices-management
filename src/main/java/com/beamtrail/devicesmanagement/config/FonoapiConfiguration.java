package com.beamtrail.devicesmanagement.config;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "fonoapi.service")
public class FonoapiConfiguration {

    @NotBlank
    private String protocol;

    @NotBlank
    private String host;

    @Value("${timeout:60000}")
    private int timeout;

    @NotBlank
    private String token;

    @NotBlank
    private String path;

}
