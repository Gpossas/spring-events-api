package com.example.api.domain.coupons;

public record CreateCouponDTO(
        String code,
        Integer discount,
        Long expiration_date
) {}
