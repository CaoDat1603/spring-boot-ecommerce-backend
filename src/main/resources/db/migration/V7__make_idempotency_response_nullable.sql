ALTER TABLE idempotency_records
    ALTER COLUMN response_status DROP NOT NULL;

ALTER TABLE idempotency_records
    ALTER COLUMN response_body DROP NOT NULL;