package org.huydd.bus_ticket_Ecommercial_platform.services;

import org.huydd.bus_ticket_Ecommercial_platform.dtos.RouteDTO;
import org.huydd.bus_ticket_Ecommercial_platform.exceptions.IdNotFoundException;
import org.huydd.bus_ticket_Ecommercial_platform.mappers.RouteDTOMapper;
import org.huydd.bus_ticket_Ecommercial_platform.repositories.FilterAndPaginateRepository;
import org.huydd.bus_ticket_Ecommercial_platform.repositories.RouteRepository;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class RouteService extends AbstractPaginateAndFilterService {
    public RouteService(RouteRepository repository, RouteDTOMapper dtoMapper) {
        super(repository, dtoMapper);
    }

    public RouteDTO getById(Long id) {
        var result = repository.findById(id);
        if (result.isPresent()) {
            return (RouteDTO) dtoMapper.apply(result.get());
        } else throw new IdNotFoundException("Id not found");

    }
}
