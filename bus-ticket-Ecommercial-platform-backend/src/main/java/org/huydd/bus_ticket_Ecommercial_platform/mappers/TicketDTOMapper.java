package org.huydd.bus_ticket_Ecommercial_platform.mappers;

import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.RouteDTO;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.SeatDTO;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.TicketDTO;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.TripDTO;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.Ticket;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.Trip;
import org.huydd.bus_ticket_Ecommercial_platform.services.RouteService;
import org.huydd.bus_ticket_Ecommercial_platform.services.TripService;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class TicketDTOMapper implements Function<Ticket, TicketDTO> {

    private final RouteService routeService;
    private final TripService tripService;

    @Override
    public TicketDTO apply(Ticket ticket) {
        TripDTO trip = (TripDTO) tripService.toDTO(ticket.getTrip());
        RouteDTO route = (RouteDTO) routeService.toDTO(ticket.getTrip().getRoute());
        SeatDTO seat = SeatDTO.builder()
                .code(ticket.getSeatCode())
                .id(null)
                .build();
        return TicketDTO.builder()
                .tripInfo(trip)
                .paymentMethod(ticket.getPaymentMethod())
                .seatPrice(ticket.getSeatPrice())
                .routeInfo(route)
                .status(ticket.getStatus())
                .luggage(ticket.getCarryOnLuggageKg())
                .seatInfo(seat)
                .id(ticket.getId())
                .build();
    }
}
