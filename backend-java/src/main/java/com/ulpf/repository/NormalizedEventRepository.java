package com.ulpf.repository;

import com.ulpf.model.NormalizedEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface NormalizedEventRepository extends MongoRepository<NormalizedEvent, String> {

    Optional<NormalizedEvent> findByEventId(String eventId);

    Page<NormalizedEvent> findByVendor(String vendor, Pageable pageable);

    Page<NormalizedEvent> findBySeverity(String severity, Pageable pageable);

    @Query("{ 'timestamp': { $gte: ?0, $lte: ?1 } }")
    Page<NormalizedEvent> findByTimeRange(Instant start, Instant end, Pageable pageable);

    long countByTimestampGreaterThanEqual(Instant since);

    long countBySeverity(String severity);

    long countByVendor(String vendor);

    List<NormalizedEvent> findTop10ByOrderByTimestampDesc();
}
