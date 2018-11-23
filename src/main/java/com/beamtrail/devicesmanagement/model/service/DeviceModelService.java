package com.beamtrail.devicesmanagement.model.service;

import java.util.List;
import java.util.Optional;

import com.beamtrail.devicesmanagement.model.Device;
import com.beamtrail.devicesmanagement.pojo.DeviceBookedResponse;

public interface DeviceModelService {

    List<Device> findAllDevices();

    List<Device> findDevices(String brand, String model);

    List<Device> findDevices(boolean isBooked);

    Optional<Device> findDevice(Long deviceId);

    Optional<DeviceBookedResponse> bookDevice(Long deviceId, String userName);

    Optional<DeviceBookedResponse> returnDevice(Long deviceId, String userName);
}
