package com.beamtrail.devicesmanagement.client.fonoapi;

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
public class FonoapiDevice {

    private String DeviceName;

    private String Brand;

    private String technology;

    private String _2g_bands;

    private String _3g_bands;

    private String _4g_bands;

}
