-- Một user chỉ có một cart
ALTER TABLE carts
    ADD CONSTRAINT uk_carts_user
        UNIQUE (user_id);

-- Một product chỉ xuất hiện một lần trong một cart
ALTER TABLE cart_items
    ADD CONSTRAINT uk_cart_items_cart_product
        UNIQUE (cart_id, product_id);