package org.huydd.bus_ticket_Ecommercial_platform.dtos;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Builder
public class TripDTO implements Serializable {
    private Long id;

    private Timestamp createdAt;

    private Boolean isActive;

    private Timestamp departAt;

    private Long routeId;

    private Long carId;
}
