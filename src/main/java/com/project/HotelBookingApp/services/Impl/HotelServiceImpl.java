package com.project.HotelBookingApp.services.Impl;

import com.project.HotelBookingApp.dtos.HotelDto;
import com.project.HotelBookingApp.dtos.HotelInfoDto;
import com.project.HotelBookingApp.dtos.RoomDto;
import com.project.HotelBookingApp.entities.Hotel;
import com.project.HotelBookingApp.entities.Room;
import com.project.HotelBookingApp.exceptions.ResourceNotFoundException;
import com.project.HotelBookingApp.repositories.HotelRepository;
import com.project.HotelBookingApp.repositories.RoomRepository;
import com.project.HotelBookingApp.services.HotelService;
import com.project.HotelBookingApp.services.InventoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;
    private final ModelMapper mapper;
    private final InventoryService inventoryService;
    private final RoomRepository roomRepository;

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
    @Transactional
    public Boolean deleteHotelById(Long id) {
        Hotel hotelEntity = hotelRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException
                                ("Hotel was not available with given id: "+ id)
                );

        //DELETE ALL INVENTORIES FOR THIS HOTEL ROOMS
        for(Room room: hotelEntity.getRooms()){
            inventoryService.deleteAllInventories(room);
            roomRepository.deleteById(room.getId());
        }
        hotelRepository.delete(hotelEntity);
        return true;
    }

    @Override
    @Transactional
    public HotelDto activateHotel(Long id) {
        log.info("Activating hotel by id: {}", id);
        Hotel hotelEntity = hotelRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException
                                ("Hotel was not available with given id: "+ id)
                );
        log.info("Fetched hotel with id: {}",hotelEntity.getId());
        hotelEntity.setActive(true);

        //create inventory for all the rooms for this hotel
        for(Room room:hotelEntity.getRooms()){
            inventoryService.initializeRoomForAYear(room);
        }

        hotelEntity = hotelRepository.save(hotelEntity);
        return mapper.map(hotelEntity,HotelDto.class);
    }

    @Override
    public HotelInfoDto getHotelInfoById(Long hotelId) {
        Hotel hotelById = hotelRepository.findById(hotelId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Hotel was not found with id: "+ hotelId));

        List<Room> rooms = hotelById.getRooms();
        Set<RoomDto> roomDtos = rooms.stream().map(
                (room)->
                    mapper.map(room,RoomDto.class))
                .collect(Collectors.toSet());

        HotelDto hotelDto = mapper.map(hotelById,HotelDto.class);

        return new HotelInfoDto(hotelDto,roomDtos);
    }
}
