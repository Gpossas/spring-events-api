package com.example.api.repositories;

import com.example.api.domain.events.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID>
{
    @Query("SELECT event FROM Event event WHERE event.date >= :current_date")
    public Page<Event> find_upcoming_events(@Param("current_date") Date current_date, Pageable pageable);
}
