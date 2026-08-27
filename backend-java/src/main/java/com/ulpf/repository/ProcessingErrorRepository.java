package com.ulpf.repository;

import com.ulpf.model.ProcessingError;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessingErrorRepository extends MongoRepository<ProcessingError, String> {
    List<ProcessingError> findByJobId(String jobId);
    Page<ProcessingError> findByJobId(String jobId, Pageable pageable);
}
