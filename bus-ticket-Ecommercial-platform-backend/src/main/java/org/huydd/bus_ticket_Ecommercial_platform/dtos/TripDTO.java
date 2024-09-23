package org.huydd.bus_ticket_Ecommercial_platform.dtos;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.Car;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.Ticket;

import java.sql.Timestamp;
import java.util.Collection;

@Data
@Builder
public class TripDTO {
    private Long id;

    private Timestamp createdAt;

    private Boolean isActive;

    private Timestamp departAt;

    private Long routeId;

    private Long carId;
}
