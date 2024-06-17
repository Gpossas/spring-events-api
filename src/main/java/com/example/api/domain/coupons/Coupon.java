package com.example.api.domain.coupons;

import com.example.api.domain.events.Event;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "coupons")
public class Coupon
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String code;

    @Column
    private Integer discount;

    @Column
    private Date expiration_date;

    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Event event;
}
