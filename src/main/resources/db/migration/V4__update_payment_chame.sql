ALTER TABLE payments
    ADD CONSTRAINT uq_payment_order
        UNIQUE (order_id);