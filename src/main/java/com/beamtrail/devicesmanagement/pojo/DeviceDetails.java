package com.beamtrail.devicesmanagement.pojo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.beamtrail.devicesmanagement.client.fonoapi.FonoapiDevice;
import com.beamtrail.devicesmanagement.model.Device;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
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

    public DeviceDetails(Device device, FonoapiDevice fonoapiDevice) {

        super();

        this.setId(device.getId());
        this.setName(fonoapiDevice.getDeviceName());
        this.setBrand(device.getBrand());
        this.setModel(device.getModel());
        this.setTechnology(fonoapiDevice.getTechnology());
        this.setBands2g(fonoapiDevice.get_2g_bands());
        this.setBands3g(fonoapiDevice.get_3g_bands());
        this.setBands4g(fonoapiDevice.get_4g_bands());
        this.setBooked(device.isBooked());
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
