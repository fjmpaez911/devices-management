package com.beamtrail.devicesmanagement.controller;

import java.util.Optional;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beamtrail.devicesmanagement.exception.DefinedErrorException;
import com.beamtrail.devicesmanagement.exception.ErrorEnum;
import com.beamtrail.devicesmanagement.model.service.DeviceModelService;
import com.beamtrail.devicesmanagement.pojo.DeviceBookedRequest;
import com.beamtrail.devicesmanagement.pojo.DeviceBookedResponse;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/v1.0/devices")
@Slf4j
public class DevicesController {

    @Autowired
    private DeviceModelService deviceModelService;

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
