package com.beamtrail.devicesmanagement.model.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.beamtrail.devicesmanagement.model.BookHistory;

@Repository
public interface BookHistoryRepository extends CrudRepository<BookHistory, Long> {

    Optional<BookHistory> findById(Long id);

    Optional<BookHistory> findTop1ByDeviceIdOrderByBookedTimestampDesc(Long deviceId);

    List<BookHistory> findTop5ByDeviceIdOrderByBookedTimestampDesc(Long deviceId);

    Optional<BookHistory> findByDeviceIdAndUserNameIgnoreCaseAndReturnedTimestampIsNull(
            Long deviceId, String userName);

}
