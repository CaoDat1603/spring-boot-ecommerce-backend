ALTER TABLE idempotency_records
    ADD COLUMN status VARCHAR(20);

UPDATE idempotency_records
SET status = 'COMPLETED'
WHERE response_status IS NOT NULL;

UPDATE idempotency_records
SET status = 'PROCESSING'
WHERE response_status IS NULL;

ALTER TABLE idempotency_records
    ALTER COLUMN status SET NOT NULL;