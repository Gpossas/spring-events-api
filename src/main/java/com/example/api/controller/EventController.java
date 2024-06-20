package com.example.api.controller;

import com.example.api.domain.events.CreateEventDTO;
import com.example.api.domain.events.Event;
import com.example.api.domain.events.EventResponseDTO;
import com.example.api.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController
{
    @Autowired
    private EventService event_service;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Event> create(
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("date") Long date,
            @RequestParam("city") String city,
            @RequestParam("uf") String uf,
            @RequestParam("is_remote") Boolean is_remote,
            @RequestParam("event_url") String event_url,
            @RequestParam(value = "image", required = false) MultipartFile image
            )
    {
        CreateEventDTO event_dto = new CreateEventDTO(title, description, date, city, uf, is_remote, event_url, image);
        return ResponseEntity.ok(this.event_service.create_event(event_dto));
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> get_events(
            @RequestParam(defaultValue = "0") Integer page_number,
            @RequestParam(defaultValue = "10") Integer page_size
    )
    {
        List<EventResponseDTO> events = this.event_service.get_upcoming_events(page_number, page_size);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<EventResponseDTO>> get_filtered_events(
            @RequestParam(defaultValue = "0") Integer page_number,
            @RequestParam(defaultValue = "10") Integer page_size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String uf,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date start_date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date end_date
    )
    {
        List<EventResponseDTO> events = this.event_service.get_filtered_events(
                page_number,
                page_size,
                title,
                city,
                uf,
                start_date,
                end_date
        );
        return ResponseEntity.ok(events);
    }
}
