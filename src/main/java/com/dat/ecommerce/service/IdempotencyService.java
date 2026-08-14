package com.dat.ecommerce.service;

import com.dat.ecommerce.entity.IdempotencyClaim;
import com.dat.ecommerce.entity.IdempotencyRecord;
import com.dat.ecommerce.entity.User;
import com.dat.ecommerce.enums.IdempotencyStatus;
import com.dat.ecommerce.repository.IdempotencyRecordRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class IdempotencyService {

    private final IdempotencyRecordRepository repository;

    public IdempotencyService(
            IdempotencyRecordRepository repository
    ) {
        this.repository = repository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public IdempotencyClaim claim(
            String key,
            User user,
            String endpoint
    ) {

        // Bước 1:
        // Kiểm tra key đã tồn tại chưa
        Optional<IdempotencyRecord> existing =
                repository.findByIdempotencyKeyAndUserIdAndEndpoint(
                        key,
                        user.getId(),
                        endpoint
                );

        if (existing.isPresent()) {

            return new IdempotencyClaim(
                    existing.get(),
                    false
            );
        }

        // Bước 2:
        // Chưa có → cố gắng tạo record
        try {

            IdempotencyRecord record =
                    new IdempotencyRecord(
                            key,
                            user,
                            endpoint
                    );

            record = repository.saveAndFlush(record);

            // Request hiện tại là owner
            return new IdempotencyClaim(
                    record,
                    true
            );

        } catch (DataIntegrityViolationException e) {

            /*
             * Request khác đã INSERT cùng key
             * trước request hiện tại.
             *
             * PostgreSQL UNIQUE constraint
             * đã chặn request này.
             */

            IdempotencyRecord record =
                    repository
                            .findByIdempotencyKeyAndUserIdAndEndpoint(
                                    key,
                                    user.getId(),
                                    endpoint
                            )
                            .orElseThrow(() ->
                                    new IllegalStateException(
                                            "Idempotency record could not be found",
                                            e
                                    )
                            );

            return new IdempotencyClaim(
                    record,
                    false
            );
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void complete(
            IdempotencyRecord record,
            int responseStatus,
            String responseBody
    ) {
        record.setStatus(
                IdempotencyStatus.COMPLETED
        );
        record.setResponseStatus(
                responseStatus
        );
        record.setResponseBody(
                responseBody
        );
        repository.save(record);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void fail(
            IdempotencyRecord record,
            int responseStatus,
            String responseBody
    ) {
        record.setStatus(
                IdempotencyStatus.FAILED
        );
        record.setResponseStatus(
                responseStatus
        );
        record.setResponseBody(
                responseBody
        );
        repository.save(record);
    }
}