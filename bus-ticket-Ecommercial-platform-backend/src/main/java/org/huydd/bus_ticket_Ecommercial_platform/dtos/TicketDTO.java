package org.huydd.bus_ticket_Ecommercial_platform.dtos;

import lombok.Builder;
import lombok.Data;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.PaymentMethod;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.TicketStatus;

@Data
@Builder
public class TicketDTO {

    private Long id;
    private RouteDTO routeInfo;
    private TripDTO tripInfo;
    private SeatDTO seatInfo;
    private Double luggage;
    private TicketStatus status;
    private Double seatPrice;
    private PaymentMethod paymentMethod;
}
