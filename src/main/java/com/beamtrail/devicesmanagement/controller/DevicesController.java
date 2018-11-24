package com.beamtrail.devicesmanagement.controller;

import java.util.Optional;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.beamtrail.devicesmanagement.exception.DefinedErrorException;
import com.beamtrail.devicesmanagement.exception.ErrorEnum;
import com.beamtrail.devicesmanagement.model.service.DeviceModelService;
import com.beamtrail.devicesmanagement.pojo.DeviceBookedRequest;
import com.beamtrail.devicesmanagement.pojo.DeviceBookedResponse;
import com.beamtrail.devicesmanagement.pojo.GetDeviceResponse;
import com.beamtrail.devicesmanagement.pojo.GetDevicesResponse;
import com.beamtrail.devicesmanagement.service.DevicesService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/v1.0/devices")
@Slf4j
public class DevicesController {

    @Autowired
    private DeviceModelService deviceModelService;

    @Autowired
    private DevicesService devicesService;

    @GetMapping(path = "")
    public GetDevicesResponse getDevices(
            @RequestParam(name = "isBooked", required = false) String isBooked,
            @RequestParam(name = "brand", required = false) String brand,
            @RequestParam(name = "model", required = false) String model) {

        log.info("received request to get devices - isBooked: {} - brand: {} - model: {}", isBooked,
                brand, model);

        return devicesService.getDevices(isBooked, brand, model);
    }

    @GetMapping(path = "/{device_id}")
    public GetDeviceResponse getDevice(@NotBlank @PathVariable("device_id") Long deviceId) {

        log.info("received request to get device {}", deviceId);

        Optional<GetDeviceResponse> optionalResponse = devicesService.getDevice(deviceId);

        return optionalResponse.orElseThrow(() -> new DefinedErrorException(ErrorEnum.NOT_FOUND));
    }

    @PostMapping(path = "/{device_id}/book")
    public DeviceBookedResponse bookDevice(@NotBlank @PathVariable("device_id") Long deviceId,
            @RequestBody DeviceBookedRequest request) {

        log.info("received request to book device {} - {}", deviceId, request);

        Optional<DeviceBookedResponse> optionalResponse = deviceModelService.bookDevice(deviceId,
                request.getBookedBy());

        return optionalResponse.orElseThrow(() -> new DefinedErrorException(ErrorEnum.NOT_FOUND));
    }

    @PostMapping(path = "/{device_id}/return")
    public DeviceBookedResponse returnDevice(@NotBlank @PathVariable("device_id") Long deviceId,
            @RequestBody DeviceBookedRequest request) {

        log.info("received request to return device {} - {}", deviceId, request);

        Optional<DeviceBookedResponse> optionalResponse = deviceModelService.returnDevice(deviceId,
                request.getBookedBy());

        return optionalResponse.orElseThrow(() -> new DefinedErrorException(ErrorEnum.NOT_FOUND));
    }

}
