package org.huydd.bus_ticket_Ecommercial_platform.mappers;

import org.huydd.bus_ticket_Ecommercial_platform.dtos.TripDTO;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.Trip;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class TripDTOMapper implements Function<Trip, TripDTO> {
    @Override
    public TripDTO apply(Trip trip) {
        return TripDTO.builder()
                .id(trip.getId())
                .carId(trip.getCar().getId())
                .createdAt(trip.getCreatedAt())
                .departAt(trip.getDepartAt())
                .isActive(trip.getIsActive())
                .routeId(trip.getRoute().getId())
                .build();
    }
}
