package com.example.api.service;

import com.example.api.domain.coupons.Coupon;
import com.example.api.domain.coupons.CreateCouponDTO;
import com.example.api.domain.events.Event;
import com.example.api.repositories.CouponRepository;
import com.example.api.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class CouponService
{
    @Autowired
    private CouponRepository coupon_repository;

    @Autowired
    private EventRepository event_repository;

    public Coupon create(CreateCouponDTO create_coupon_dto, UUID event_id)
    {
        Event event = event_repository.findById(event_id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found") );

        Coupon coupon = new Coupon();
        coupon.setCode(create_coupon_dto.code());
        coupon.setDiscount(create_coupon_dto.discount());
        coupon.setEvent(event);

        Long expiration_date = create_coupon_dto.expiration_date();
        if (expiration_date != null)
        {
            coupon.setExpiration_date(new Date(expiration_date));
        }

        return coupon_repository.save(coupon);
    }
}
