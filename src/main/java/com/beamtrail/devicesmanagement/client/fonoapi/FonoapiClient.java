package com.beamtrail.devicesmanagement.client.fonoapi;

public interface FonoapiClient {

    FonoapiResponse getDevices(String brand, String device);

}
