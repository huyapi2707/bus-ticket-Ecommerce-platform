package org.huydd.bus_ticket_Ecommercial_platform.dtos;

import lombok.Builder;
import lombok.Data;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.Station;

import java.io.Serializable;
import java.util.Collection;

@Builder
@Data
public class RouteDTO implements Serializable {
    private Long id;
    private String name;
    private BusCompanyDTO company;

    private Boolean isActive;
    private Double seatPrice;

    private Station fromStation;
    private Collection<Station> pickUpPoints;
    private Station toStation;
}
