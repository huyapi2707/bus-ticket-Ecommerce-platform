package org.huydd.bus_ticket_Ecommercial_platform.services;

import org.huydd.bus_ticket_Ecommercial_platform.mappers.TripDTOMapper;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.Trip;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.TripSeatInfo;
import org.huydd.bus_ticket_Ecommercial_platform.repositories.TripRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripService extends AbstractPaginateAndFilterService {

    private TripRepository tripRepository;
    private TripDTOMapper tripDTOMapper;

    private TripSeatInfoService tripSeatInfoService;

    public TripService(TripRepository tripRepository,
                       TripDTOMapper tripDTOMapper,
                       TripSeatInfoService tripSeatInfoService) {
        super(tripRepository, tripDTOMapper);
        this.tripRepository = tripRepository;
        this.tripDTOMapper = tripDTOMapper;
        this.tripSeatInfoService = tripSeatInfoService;
    }



    public List<Trip> getByRouteId(Long routeId) {
        return tripRepository.findAllByRouteIdAndIsActive(routeId, true);

    }

    public TripSeatInfo getTripSeatInfoByTripId(Long tripId, Boolean isActive) {
        return tripSeatInfoService.getByTripId(tripId, isActive);
    }



}
