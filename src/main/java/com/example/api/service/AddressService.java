package com.example.api.service;

import com.example.api.domain.address.Address;
import com.example.api.domain.address.CreateAddressDTO;
import com.example.api.domain.events.CreateEventDTO;
import com.example.api.domain.events.Event;
import com.example.api.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService
{
    @Autowired
    private AddressRepository address_repository;

    public Address create(CreateEventDTO dto, Event event)
    {
        Address address = new Address();
        address.setUf(dto.uf());
        address.setCity(dto.city());
        address.setEvent(event);

        return address_repository.save(address);
    }
}
