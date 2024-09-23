package org.huydd.bus_ticket_Ecommercial_platform.services;

import org.huydd.bus_ticket_Ecommercial_platform.dtos.BusCompanyDTO;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.RouteDTO;
import org.huydd.bus_ticket_Ecommercial_platform.exceptions.IdNotFoundException;
import org.huydd.bus_ticket_Ecommercial_platform.mappers.BusCompanyDTOMapper;
import org.huydd.bus_ticket_Ecommercial_platform.mappers.RouteDTOMapper;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.BusCompany;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.Route;
import org.huydd.bus_ticket_Ecommercial_platform.repositories.BusCompanyRepository;

import org.huydd.bus_ticket_Ecommercial_platform.specifications.RouteSpecification;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BusCompanyService extends AbstractPaginateAndFilterService {

    private  RouteService routeService;
    private BusCompanyRepository busCompanyRepository;

    private BusCompanyDTOMapper  busCompanyDTOMapper;

    public BusCompanyService(BusCompanyRepository busCompanyRepository,
                             BusCompanyDTOMapper  busCompanyDTOMapper,
                             RouteService routeService) {
        super(busCompanyRepository, busCompanyDTOMapper);
        this.routeService = routeService;
        this.busCompanyRepository = busCompanyRepository;
        this.busCompanyDTOMapper = busCompanyDTOMapper;
    }


    public BusCompanyDTO getById(Long id) {
        var result = busCompanyRepository.findById(id);
        if (result.isPresent()) {
            return  busCompanyDTOMapper.apply(result.get());
        }
        else throw new IdNotFoundException("Id not found");
    }

    public Object getRoutes(Long id) {
      return routeService.getByBusCompanyId(id);
    }
}
