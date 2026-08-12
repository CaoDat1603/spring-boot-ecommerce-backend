ALTER TABLE payments
    ALTER COLUMN paid_at DROP NOT NULL;

ALTER TABLE payments
    ADD COLUMN provider VARCHAR(30) NOT NULL DEFAULT 'STRIPE';

ALTER TABLE payments
    ADD COLUMN provider_payment_id VARCHAR(255);

CREATE UNIQUE INDEX uq_payment_provider_payment
    ON payments(provider, provider_payment_id)
    WHERE provider_payment_id IS NOT NULL;