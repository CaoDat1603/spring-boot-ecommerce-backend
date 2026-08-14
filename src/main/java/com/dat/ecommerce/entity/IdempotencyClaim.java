package com.dat.ecommerce.entity;

// owner = true
//→ request hiện tại thắng
//→ được phép xử lý payment
//
//owner = false
//→ key đã tồn tại
//→ không được tạo payment lần nữa
public record IdempotencyClaim(
        IdempotencyRecord record,
        boolean owner
) {
}
