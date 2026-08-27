package com.ulpf.repository;

import com.ulpf.model.ProcessingJob;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProcessingJobRepository extends MongoRepository<ProcessingJob, String> {
    Optional<ProcessingJob> findByJobId(String jobId);
}
