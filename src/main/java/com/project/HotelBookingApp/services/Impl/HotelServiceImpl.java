package com.project.HotelBookingApp.services.Impl;

import com.project.HotelBookingApp.dtos.HotelDto;
import com.project.HotelBookingApp.entities.Hotel;
import com.project.HotelBookingApp.exceptions.ResourceNotFoundException;
import com.project.HotelBookingApp.repositories.HotelRepository;
import com.project.HotelBookingApp.services.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;
    private final ModelMapper mapper;
//    private final Logger logger;

    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("Creating new hotel with name: {}", hotelDto.getName());
        Hotel hotelEntity = mapper.map(hotelDto, Hotel.class);
        hotelEntity.setActive(false);
        Hotel savedHotel = hotelRepository.save(hotelEntity);
        return mapper.map(savedHotel, HotelDto.class);
    }

    @Override
    public HotelDto getHotelById(Long id) {
        log.info("Get hotel by id: {}", id);
        Hotel hotelEntity = hotelRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException
                                ("Hotel was not available with given id: "+ id)
                );
        return mapper.map(hotelEntity,HotelDto.class);
    }
}
