package com.beamtrail.devicesmanagement.client.fonoapi;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FonoapiResponse {

    @JsonProperty("DeviceName")
    private String deviceName;

    @JsonProperty("Brand")
    private String brand;

    private String technology;

    @JsonProperty("_2g_bands")
    private String bands2g;

    @JsonProperty("_3g_bands")
    private String bands3g;

    @JsonProperty("_4g_bands")
    private String bands4g;

}
