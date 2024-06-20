package com.example.api.service;

import com.amazonaws.services.s3.AmazonS3;
import com.example.api.domain.events.CreateEventDTO;
import com.example.api.domain.events.Event;
import com.example.api.domain.events.EventResponseDTO;
import com.example.api.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.Calendar;

@Service
public class EventService
{
    @Autowired
    private AmazonS3 s3_client;

    @Value("${aws.bucket.name}")
    private String bucket_name;

    @Autowired
    private EventRepository event_repository;

    @Autowired
    private AddressService address_service;

    public Event create_event(CreateEventDTO create_event_dto)
    {
        String image_url = null;

        if (create_event_dto.image() != null)
        {
            image_url = this.upload_image(create_event_dto.image());
        }

        Event event = new Event();
        event.setTitle(create_event_dto.title());
        event.setDescription(create_event_dto.description());
        event.setEvent_url(create_event_dto.event_url());
        event.setDate(new Date(create_event_dto.date()));
        event.setImage_url(image_url);
        event.setIs_remote(create_event_dto.is_remote());

        event_repository.save(event);

        if (!event.getIs_remote())
        {
            address_service.create(create_event_dto, event);
        }

        return event;
    }

    public List<EventResponseDTO> get_upcoming_events(Integer page_number, Integer page_size)
    {
        Pageable pageable = PageRequest.of(page_number,page_size);
        Page<Event> events_page = this.event_repository.find_upcoming_events(new Date(), pageable);
        return events_page.map(event -> new EventResponseDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                event.getAddress() != null ? event.getAddress().getCity() : "",
                event.getAddress() != null ? event.getAddress().getUf() : "",
                event.getIs_remote(),
                event.getImage_url(),
                event.getEvent_url()
        )).stream().toList();
    }

    public List<EventResponseDTO> get_filtered_events(
            Integer page_number,
            Integer page_size,
            String title,
            String city,
            String uf,
            Date start_date,
            Date end_date
    )
    {
        title = title != null ? title : "";
        city = city != null ? city : "";
        uf = uf != null ? uf : "";
        start_date = start_date != null ? start_date : new Date();

        // Set the year, month, and day to 2099-01-01
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2099);
        calendar.set(Calendar.MONTH, Calendar.JANUARY); // Months are zero-based in Calendar
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date dateIn2099 = calendar.getTime();
        end_date = end_date != null ? end_date : dateIn2099; //todo: this is getting the data of 1970 because it's too long to put the current or 10 years in future

        Pageable pageable = PageRequest.of(page_number,page_size);
        Page<Event> events_page = this.event_repository.find_filtered_events(
                title,
                city,
                uf,
                start_date,
                end_date,
                pageable
        );
        return events_page.map(event -> new EventResponseDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                event.getAddress() != null ? event.getAddress().getCity() : "",
                event.getAddress() != null ? event.getAddress().getUf() : "",
                event.getIs_remote(),
                event.getImage_url(),
                event.getEvent_url()
        )).stream().toList();
    }

    private String upload_image(MultipartFile multipart_file)
    {
        String file_name = UUID.randomUUID() + multipart_file.getOriginalFilename();

        try
        {
            File file = this.convert_multipart_to_file(multipart_file);
            s3_client.putObject(bucket_name, file_name, file);
            file.delete();
            return s3_client.getUrl(bucket_name, file_name).toString();
        }
        catch (Exception e)
        {
            System.out.println(e);
            System.out.println("Error uploading file");
            return null;
        }
    }

    private File convert_multipart_to_file(MultipartFile multipart_file) throws IOException
    {
        File converted_file = new File(Objects.requireNonNull(multipart_file.getOriginalFilename()));
        FileOutputStream file_output_stream = new FileOutputStream(converted_file);
        file_output_stream.write(multipart_file.getBytes());
        file_output_stream.close();
        return converted_file;
    }
}
