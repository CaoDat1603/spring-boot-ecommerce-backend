package com.dat.ecommerce.service;

import com.dat.ecommerce.entity.IdempotencyRecord;
import com.dat.ecommerce.entity.User;
import com.dat.ecommerce.repository.IdempotencyRecordRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IdempotencyService {

    private final IdempotencyRecordRepository repository;

    public IdempotencyService(
            IdempotencyRecordRepository repository
    ) {
        this.repository = repository;
    }

    public Optional<IdempotencyRecord> findExisting(
            String key,
            Long userId,
            String endpoint
    ) {
        return repository
                .findByIdempotencyKeyAndUserIdAndEndpoint(
                        key,
                        userId,
                        endpoint
                );
    }

    public IdempotencyRecord create(
            String key,
            User user,
            String endpoint
    ) {

        IdempotencyRecord record =
                new IdempotencyRecord(
                        key,
                        user,
                        endpoint
                );

        return repository.save(record);
    }

    public void saveResponse(
            IdempotencyRecord record,
            int status,
            String responseBody
    ) {

        record.setResponseStatus(status);
        record.setResponseBody(responseBody);

        repository.save(record);
    }
}