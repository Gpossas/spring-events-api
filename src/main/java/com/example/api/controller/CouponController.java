package com.example.api.controller;

import com.example.api.domain.coupons.Coupon;
import com.example.api.domain.coupons.CreateCouponDTO;
import com.example.api.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/coupons")
public class CouponController
{
    @Autowired
    private CouponService coupon_service;

    @PostMapping("/events/{event_id}")
    public Coupon create(@RequestBody CreateCouponDTO create_coupon_dto, @PathVariable UUID event_id)
    {
        return ResponseEntity.ok(coupon_service.create(create_coupon_dto, event_id)).getBody();
    }
}
