package org.huydd.bus_ticket_Ecommercial_platform.services;

import org.huydd.bus_ticket_Ecommercial_platform.dtos.RouteDTO;
import org.huydd.bus_ticket_Ecommercial_platform.exceptions.IdNotFoundException;
import org.huydd.bus_ticket_Ecommercial_platform.mappers.RouteDTOMapper;
import org.huydd.bus_ticket_Ecommercial_platform.repositories.FilterAndPaginateRepository;
import org.huydd.bus_ticket_Ecommercial_platform.repositories.RouteRepository;
import org.huydd.bus_ticket_Ecommercial_platform.specifications.TripSpecification;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RouteService extends AbstractPaginateAndFilterService {

    private TripService tripService;

    private RouteRepository routeRepository;

    private RouteDTOMapper routeDTOMapper;
    public RouteService(RouteRepository routeRepository,
                        RouteDTOMapper routeDTOMapper,
                        TripService tripService) {
        super(routeRepository, routeDTOMapper);
        this.routeRepository = routeRepository;
        this.tripService = tripService;
        this.routeDTOMapper = routeDTOMapper;
    }




    public Object getTrips(Long id) {
        return tripService.getByRouteId(id);
    }

    public Object getByBusCompanyId(Long busCompanyId) {
        return routeRepository.findAllByCompanyIdAndIsActive(busCompanyId, true)
                .stream().map(routeDTOMapper)
                .collect(Collectors.toList());
    }
}
