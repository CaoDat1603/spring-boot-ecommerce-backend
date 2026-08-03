package com.dat.ecommerce.repository;

import com.dat.ecommerce.entity.IdempotencyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IdempotencyRecordRepository extends JpaRepository<IdempotencyRecord, Long> {
    Optional<IdempotencyRecord> findByIdempotencyKeyAndRequestPath(String idempotencyKey, String requestPath);
    long countByIdempotencyKeyAndRequestPath(String idempotencyKey, String requestPath);
}
