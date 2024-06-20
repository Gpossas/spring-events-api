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

    @Query("SELECT event FROM Event event " +
            "LEFT JOIN e.address address " +
            "WHERE e.date >= :current_date AND " +
            "(:title IS NULL OR event.title LIKE %:title%) AND " +
            "(:city IS NULL OR event.city LIKE %:city%) AND " +
            "(:uf IS NULL OR event.uf LIKE %:uf%) AND " +
            "(:start_date IS NULL OR event.start_date >= %:start_date%) AND " +
            "(:end_date IS NULL OR event.end_date <= %:end_date%)"
    )
    public Page<Event> find_filtered_events(
            @Param("current_date") Date current_date,
            @Param("title") String title,
            @Param("city") String city,
            @Param("uf") String uf,
            @Param("start_date") Date start_date,
            @Param("end_date") Date end_date,
            Pageable pageable
    );
}
