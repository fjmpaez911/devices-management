package com.beamtrail.devicesmanagement.model.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.beamtrail.devicesmanagement.model.Device;

@Repository
public interface DeviceRepository extends CrudRepository<Device, Long> {

    Optional<Device> findById(Long id);

    List<Device> findAll();

    List<Device> findByBrandIgnoreCase(String brand);

    List<Device> findByModelIgnoreCase(String model);

    List<Device> findByBookedTrue();

    List<Device> findByBookedFalse();

}
