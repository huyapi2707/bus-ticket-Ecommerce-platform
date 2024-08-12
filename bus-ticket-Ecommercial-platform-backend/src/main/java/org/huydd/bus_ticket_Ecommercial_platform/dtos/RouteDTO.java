package org.huydd.bus_ticket_Ecommercial_platform.dtos;

import lombok.Builder;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.Station;

import java.util.Collection;
import java.util.List;

@Builder
public record RouteDTO(Long id,
                       String name,
                       BusCompanyDTO company,

                       Station fromStation,
                       Collection<Station> pickUpPoints,
                       Station toStation) {
}
