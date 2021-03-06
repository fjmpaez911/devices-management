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
public class GetDeviceResponse extends DeviceDetails {

    @JsonProperty("last_booked_by")
    private String lastBookedBy;

    @JsonProperty("last_booked_timestamp")
    private long lastBookedTimestamp;

    @JsonProperty("correlation_id")
    private String correlationId;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public GetDeviceResponse(Device device, FonoapiDevice fonoapiDevice) {
        super(device, fonoapiDevice);
    }

}
