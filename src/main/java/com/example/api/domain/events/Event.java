package com.example.api.domain.events;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "events")
public class Event
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column(nullable = false)
    private Date date;

    @Column
    private String image_url;

    @Column
    private String event_url;

    @Column
    private Boolean is_remote;
}
