package org.huydd.bus_ticket_Ecommercial_platform.mappers;

import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.RouteDTO;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.Route;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class RouteDTOMapper implements Function<Route, RouteDTO> {

    private final BusCompanyDTOMapper busCompanyDTOMapper;

    @Override
    public RouteDTO apply(Route route) {
        return RouteDTO.builder()
                .id(route.getId())
                .isActive(route.getIsActive())
                .name(route.getName())
                .seatPrice(route.getSeatPrice())
                .fromStation(route.getFromStation())
                .pickUpPoints(route.getPickUpPoints())
                .toStation(route.getToStation())
                .company(busCompanyDTOMapper.apply(route.getCompany()))
                .build();
    }
}
