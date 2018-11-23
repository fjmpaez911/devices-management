package com.beamtrail.devicesmanagement.client.fonoapi;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FonoapiResponse {

    private List<FonoapiDevice> devices = new ArrayList<>();

}
