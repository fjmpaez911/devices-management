package com.beamtrail.devicesmanagement.pojo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDetails {

    private long id;

    private String name;

    private String brand;

    private String model;

    private String technology;

    @JsonProperty("bands_2g")
    private String bands2g;

    @JsonProperty("bands_3g")
    private String bands3g;

    @JsonProperty("bands_4g")
    private String bands4g;

    private boolean booked;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
