package com.beamtrail.devicesmanagement.service;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.SpanAccessor;
import org.springframework.stereotype.Service;

import com.beamtrail.devicesmanagement.client.fonoapi.FonoapiClient;
import com.beamtrail.devicesmanagement.client.fonoapi.FonoapiDevice;
import com.beamtrail.devicesmanagement.client.fonoapi.FonoapiResponse;
import com.beamtrail.devicesmanagement.exception.DefinedErrorException;
import com.beamtrail.devicesmanagement.exception.ErrorEnum;
import com.beamtrail.devicesmanagement.model.Device;
import com.beamtrail.devicesmanagement.model.service.DeviceModelService;
import com.beamtrail.devicesmanagement.pojo.DeviceDetails;
import com.beamtrail.devicesmanagement.pojo.GetDeviceResponse;
import com.beamtrail.devicesmanagement.pojo.GetDevicesResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DevicesServiceImpl implements DevicesService {

    @Autowired
    private SpanAccessor spanAccessor;

    @Autowired
    private DeviceModelService deviceModelService;

    @Autowired
    private FonoapiClient fonoapiClient;

    @Override
    public GetDevicesResponse getDevices(String isBooked, String brand, String model) {

        GetDevicesResponse response = new GetDevicesResponse();
        response.setCorrelationId(spanAccessor.getCurrentSpan().traceIdString());

        List<Device> devices = null;

        if (StringUtils.isBlank(isBooked) && StringUtils.isBlank(brand)
                && StringUtils.isBlank(model)) {

            devices = deviceModelService.findAllDevices();

        } else if (StringUtils.isNotBlank(brand) || StringUtils.isNotBlank(model)) {

            devices = deviceModelService.findDevices(brand, model);

            if (StringUtils.isNotBlank(isBooked)) {
                devices.retainAll(deviceModelService.findDevices(Boolean.parseBoolean(isBooked)));
            }

        } else {
            devices = deviceModelService.findDevices(Boolean.parseBoolean(isBooked));
        }

        devices.parallelStream().forEach(device -> {

            FonoapiDevice fonoapiDevice = getFonoapiDevice(device);

            response.add(new DeviceDetails(device, fonoapiDevice));
        });

        return response;
    }

    @Override
    public Optional<GetDeviceResponse> getDevice(Long deviceId) {

        GetDeviceResponse response = null;

        Optional<Device> deviceOptional = deviceModelService.findDevice(deviceId);

        if (deviceOptional.isPresent()) {

            Device device = deviceOptional.get();

            FonoapiDevice fonoapiDevice = getFonoapiDevice(device);

            if (fonoapiDevice != null) {
                response = buildGetDeviceResponse(device, fonoapiDevice);
            }
        }

        return Optional.ofNullable(response);
    }

    private FonoapiDevice getFonoapiDevice(Device device) {

        FonoapiDevice fonoapiDevice = null;

        FonoapiResponse fonoapiResponse = fonoapiClient.getDevices(device.getBrand(),
                device.getModel());

        if (fonoapiResponse != null) {

            fonoapiDevice = fonoapiResponse.getDevices().stream().filter(
                    item -> StringUtils.endsWithIgnoreCase(item.getDeviceName(), device.getModel()))
                    .findFirst().orElseThrow(() -> new DefinedErrorException(ErrorEnum.NOT_FOUND));

        }

        return fonoapiDevice;
    }

    private GetDeviceResponse buildGetDeviceResponse(Device device, FonoapiDevice fonoapiDevice) {

        GetDeviceResponse response = new GetDeviceResponse(device, fonoapiDevice);

        response.setCorrelationId(spanAccessor.getCurrentSpan().traceIdString());

        deviceModelService.findLastBooking(device.getId()).ifPresent(item -> {
            response.setLastBookedBy(item.getUserName());
            response.setLastBookedTimestamp(item.getBookedTimestamp().getTime());
        });

        return response;
    }

}
