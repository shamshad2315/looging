package com.ulpf.repository;

import com.ulpf.model.RawLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RawLogRepository extends MongoRepository<RawLog, String> {
    Optional<RawLog> findByHash(String hash);
    boolean existsByHash(String hash);
}
