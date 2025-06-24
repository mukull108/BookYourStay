package com.project.HotelBookingApp.services.Impl;

import com.project.HotelBookingApp.dtos.HotelDto;
import com.project.HotelBookingApp.entities.Hotel;
import com.project.HotelBookingApp.exceptions.ResourceNotFoundException;
import com.project.HotelBookingApp.repositories.HotelRepository;
import com.project.HotelBookingApp.services.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
        log.info("Fetching hotel by id: {}", id);
        Hotel hotelEntity = hotelRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException
                                ("Hotel was not available with given id: "+ id)
                );
        return mapper.map(hotelEntity,HotelDto.class);
    }

    @Override
    public HotelDto updateHotelById(Long id, HotelDto hotelDto) {
        log.info("Updating hotel by id: {}", id);
        Hotel hotelEntity = hotelRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException
                                ("Hotel was not available with given id: "+ id)
                );
        mapper.map(hotelDto,hotelEntity); //mapping everything from hotelDto to entity
        hotelEntity.setId(id);
        Hotel savedHotel = hotelRepository.save(hotelEntity);
        return mapper.map(savedHotel,HotelDto.class);
    }

    @Override
    public Boolean deleteHotelById(Long id) {
        boolean exists = hotelRepository.
                existsById(id);
        if(!exists) throw new ResourceNotFoundException("Hotel doesn't exist with given Id!");
        hotelRepository.deleteById(id);
        //TODO: DELETE THE FUTURE INVENTORIES FOR THIS HOTEL
        return true;
    }

    @Override
    public HotelDto activateHotel(Long id) {
        log.info("Updating hotel by id: {}", id);
        Hotel hotelEntity = hotelRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException
                                ("Hotel was not available with given id: "+ id)
                );
        hotelEntity.setActive(true);
        //Todo: create inventory for all the rooms for this hotel
        Hotel hotel = hotelRepository.save(hotelEntity);
        return mapper.map(hotel,HotelDto.class);
    }
}
