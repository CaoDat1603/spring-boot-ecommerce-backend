ALTER TABLE payments
    ADD COLUMN provider_session_id VARCHAR(255);

CREATE UNIQUE INDEX uk_payments_provider_session_id
    ON payments(provider_session_id);