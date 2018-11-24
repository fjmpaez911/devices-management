package com.beamtrail.devicesmanagement.service;

import java.util.Optional;

import com.beamtrail.devicesmanagement.pojo.GetDeviceResponse;
import com.beamtrail.devicesmanagement.pojo.GetDevicesResponse;

public interface DevicesService {

    GetDevicesResponse getDevices(String isBooked, String brand, String model);

    Optional<GetDeviceResponse> getDevice(Long deviceId);

}
