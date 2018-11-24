package com.beamtrail.devicesmanagement.model.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.SpanAccessor;
import org.springframework.stereotype.Service;

import com.beamtrail.devicesmanagement.exception.DefinedErrorException;
import com.beamtrail.devicesmanagement.exception.ErrorEnum;
import com.beamtrail.devicesmanagement.model.BookHistory;
import com.beamtrail.devicesmanagement.model.Device;
import com.beamtrail.devicesmanagement.model.repo.BookHistoryRepository;
import com.beamtrail.devicesmanagement.model.repo.DeviceRepository;
import com.beamtrail.devicesmanagement.pojo.BookHistoryEntry;
import com.beamtrail.devicesmanagement.pojo.DeviceBookedResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DeviceModelServiceImpl implements DeviceModelService {

    @Autowired
    private SpanAccessor spanAccessor;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private BookHistoryRepository bhRepository;

    @Override
    public List<Device> findAllDevices() {

        return deviceRepository.findAll();
    }

    @Override
    public List<Device> findDevices(String brand, String model) {

        List<Device> devices = new ArrayList<>();

        if (StringUtils.isBlank(brand)) {

            if (StringUtils.isNotBlank(model)) {
                log.debug("searching by model: {}", model);
                devices.addAll(deviceRepository.findByModelIgnoreCase(model.trim()));
            }

        } else {

            if (StringUtils.isNotBlank(brand)) {
                log.debug("searching by brand: {}", brand);
                devices.addAll(deviceRepository.findByBrandIgnoreCase(brand.trim()));
            }

            if (StringUtils.isNotBlank(model)) {
                log.debug("searching by model: {}", model);
                devices.retainAll(deviceRepository.findByModelIgnoreCase(model.trim()));
            }
        }

        return devices;
    }

    @Override
    public List<Device> findDevices(boolean isBooked) {

        return isBooked ? deviceRepository.findByBookedTrue()
                : deviceRepository.findByBookedFalse();
    }

    @Override
    public Optional<Device> findDevice(Long deviceId) {

        return deviceRepository.findById(deviceId);
    }

    @Override
    public Optional<DeviceBookedResponse> bookDevice(Long deviceId, String userName) {

        log.debug("booking device {} by {}", deviceId, userName);

        deviceRepository.findById(deviceId).ifPresent(device -> {

            if (!device.isBooked()) {

                bhRepository.save(new BookHistory(deviceId, userName));

                device.setBooked(true);
                deviceRepository.save(device);

                log.debug("device {} successfully booked", deviceId);
            } else {
                throw new DefinedErrorException(ErrorEnum.ALREADY_BOOKED);
            }
        });

        return Optional.ofNullable(buildDeviceBookedResponse(deviceId));
    }

    @Override
    public Optional<DeviceBookedResponse> returnDevice(Long deviceId, String userName) {

        log.debug("returning device {} by {}", deviceId, userName);

        deviceRepository.findById(deviceId).ifPresent(device -> {

            BookHistory bookHistory = bhRepository
                    .findByDeviceIdAndUserNameIgnoreCaseAndReturnedTimestampIsNull(device.getId(),
                            userName)
                    .orElseThrow(() -> new DefinedErrorException(ErrorEnum.ALREADY_RETURNED));

            bookHistory.setReturnedTimestamp(new Timestamp(new Date().getTime()));
            bhRepository.save(bookHistory);

            device.setBooked(false);
            deviceRepository.save(device);

            log.debug("device {} successfully returned", deviceId);

        });

        return Optional.ofNullable(buildDeviceBookedResponse(deviceId));
    }

    @Override
    public Optional<BookHistory> findLastBooking(Long deviceId) {

        return bhRepository.findTop1ByDeviceIdOrderByBookedTimestampDesc(deviceId);
    }

    private DeviceBookedResponse buildDeviceBookedResponse(Long deviceId) {

        DeviceBookedResponse response = null;

        Optional<Device> deviceOptional = deviceRepository.findById(deviceId);

        if (deviceOptional.isPresent()) {

            Device device = deviceOptional.get();

            List<BookHistoryEntry> bhEntries = new ArrayList<>();

            bhRepository.findTop5ByDeviceIdOrderByBookedTimestampDesc(device.getId()).stream()
                    .forEach(item -> bhEntries
                            .add(new BookHistoryEntry(item.getId(), item.getUserName(),
                                    item.getBookedTimestamp(), item.getReturnedTimestamp())));

            response = DeviceBookedResponse.builder().id(device.getId()).name(device.getName())
                    .brand(device.getBrand()).model(device.getModel()).booked(device.isBooked())
                    .bookedhistory(bhEntries)
                    .correlationId(spanAccessor.getCurrentSpan().traceIdString()).build();
        }

        return response;
    }

}
