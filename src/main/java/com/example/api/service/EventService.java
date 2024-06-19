package com.example.api.service;

import com.amazonaws.services.s3.AmazonS3;
import com.example.api.domain.events.CreateEventDTO;
import com.example.api.domain.events.Event;
import com.example.api.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
public class EventService
{
    @Autowired
    private AmazonS3 s3_client;

    @Value("${aws.bucket.name}")
    private String bucket_name;

    @Autowired
    private EventRepository event_repository;

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

        return event;
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
