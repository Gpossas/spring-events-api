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
    @Query("SELECT event FROM Event event LEFT JOIN FETCH event.address address WHERE event.date >= :current_date")
    public Page<Event> find_upcoming_events(@Param("current_date") Date current_date, Pageable pageable);

    @Query("SELECT event FROM Event event " +
            "LEFT JOIN event.address address " +
            "WHERE (:title = '' OR event.title LIKE %:title%) " +
            "AND (:city = '' OR address.city LIKE %:city%) " +
            "AND (:uf = '' OR address.uf LIKE %:uf%) " +
            "AND (event.date >= :start_date AND event.date >= %:end_date%)"
    )
    public Page<Event> find_filtered_events(
            @Param("title") String title,
            @Param("city") String city,
            @Param("uf") String uf,
            @Param("start_date") Date start_date,
            @Param("end_date") Date end_date,
            Pageable pageable
    );
}
