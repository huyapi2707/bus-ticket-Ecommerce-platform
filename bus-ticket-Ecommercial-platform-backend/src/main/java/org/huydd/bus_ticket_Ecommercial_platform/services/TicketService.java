package org.huydd.bus_ticket_Ecommercial_platform.services;

import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.*;
import org.huydd.bus_ticket_Ecommercial_platform.exceptions.IdNotFoundException;
import org.huydd.bus_ticket_Ecommercial_platform.exceptions.NoPermissionException;
import org.huydd.bus_ticket_Ecommercial_platform.mappers.TicketDTOMapper;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.*;
import org.huydd.bus_ticket_Ecommercial_platform.repositories.TicketRepository;
import org.huydd.bus_ticket_Ecommercial_platform.requestObjects.TicketRequest;
import org.huydd.bus_ticket_Ecommercial_platform.responseObjects.CheckoutResponse;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.TicketDTO;
import org.huydd.bus_ticket_Ecommercial_platform.responseObjects.OnlinePaymentResponse;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class TicketService {

    private final RouteService routeService;
    private final TripService tripService;

    private final PaymentMethodService paymentMethodService;
    private final TicketRepository ticketRepository;

    private final PaymentService paymentService;

    private final TripSeatInfoService tripSeatInfoService;

    private final TicketStatusService ticketStatusService;

    private final TicketDTOMapper ticketDTOMapper;


    public void saveAll(List<Ticket> tickets) {
        ticketRepository.saveAll(tickets);
    }


    public TicketDTO toDTO(Ticket ticket) {
        return ticketDTOMapper.apply(ticket);
    }

    public List<TicketDTO> handleCartInfo(List<TicketRequest> payload) {
        return payload.stream().map(data -> {
            TripDTO thisTrip = (TripDTO) tripService.toDTO(tripService.getById(data.getTripId()));
            RouteDTO thisRoute = (RouteDTO) routeService.toDTO(routeService.getById(thisTrip.getRouteId()));
            SeatDTO thisSeat = SeatDTO.builder()
                    .id(data.getSeatInfo().get("id"))
                    .code(data.getSeatInfo().get("code").toString())
                    .build();
            return TicketDTO.builder()
                    .tripInfo(thisTrip)
                    .routeInfo(thisRoute)
                    .seatInfo(thisSeat)
                    .luggage(Double.parseDouble(data.getSeatInfo().get("luggage").toString()))
                    .build();
        }).collect(Collectors.toList());
    }


    public CheckoutResponse checkout(List<TicketRequest> cart, Long paymentMethodId, String ip) throws UnsupportedEncodingException {
        List<Ticket> tickets = createTickets(cart, paymentMethodId, null);
        List<TicketDTO> ticketDTOList = tickets.stream().map(ticketDTOMapper::apply).collect(Collectors.toList());
        String paymentUrl = null;
        PaymentMethod paymentMethod = paymentMethodService.getById(paymentMethodId);
        ticketRepository.saveAll(tickets);
        if (paymentMethod.getName().equals("VNPAY")) {
            paymentUrl = paymentService.createVnPayPaymentUrl(tickets, ip);
        }
        return CheckoutResponse.builder()
                .tickets(ticketDTOList).paymentUrl(paymentUrl).build();
    }

    public TicketDTO handleCancelTicket(Long ticketId) {
        Ticket ticket = cancelTicket(ticketId);
        return toDTO(ticket);
    }


    private Ticket cancelTicket(Long ticketId) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);
        if (optionalTicket.isEmpty()) throw new IdNotFoundException("Ticket id not found");
        Ticket ticket = optionalTicket.get();

        if (!checkPermission(ticket)) throw  new NoPermissionException("You don't have any permission to access this object");

        if (ticket.getStatus().getName().equals(ticketStatusService.getCANCELED()))
            throw new IllegalArgumentException("Ticket is already canceled");
        if (ticket.getStatus().getName().equals(ticketStatusService.getABSENTED()))
            throw new IllegalArgumentException("Ticket is absented");
        if (ticket.getStatus().getName().equals(ticketStatusService.getPAID()))
            throw new IllegalArgumentException("Ticket is paid");
        if (ticket.getStatus().getName().equals(ticketStatusService.getCOMPLETED()))
            throw new IllegalArgumentException("Ticket is completed");

        String seatCode = ticket.getSeatCode();
        Long tripId = ticket.getTrip().getId();

        tripSeatInfoService.handleRemoveTicket(tripId, seatCode);

        TicketStatus canceledStatus = ticketStatusService.getCanceledStatus();
        ticket.setStatus(canceledStatus);

        ticketRepository.save(ticket);

        return ticket;

    }


    private List<Ticket> createTickets(List<TicketRequest> cart, Long paymentMethodId, User user) {
        List<Ticket> tickets = new ArrayList<>();
        PaymentMethod paymentMethod = paymentMethodService.getById(paymentMethodId);
        User finalUser = user != null ? user : (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TicketStatus ticketStatus = ticketStatusService.getUnpaidStatus();
        cart.forEach(c -> {
            Long tripId = c.getTripId();
            Long seatId = c.getSeatInfo().get("id");

            SeatDTO thisSeat = tripSeatInfoService.handleNewTicket(tripId, seatId);

            Trip thisTrip = (Trip) tripService.getById(tripId);
            Ticket thisTicket = Ticket.builder()
                    .trip(thisTrip)
                    .customer(finalUser)
                    .seatCode(thisSeat.getCode())
                    .status(ticketStatus)
                    .carryOnLuggageKg(Double.parseDouble(c.getSeatInfo().get("luggage").toString()))
                    .seatPrice(thisTrip.getRoute().getSeatPrice())
                    .paymentMethod(paymentMethod)
                    .build();
            tickets.add(thisTicket);
        });
        ticketRepository.saveAll(tickets);
        return tickets;
    }

    private boolean checkPermission(Ticket ticket) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (ticket.getCustomer().getId().equals(user.getId())) {
            return true;
        }
        return false;
    }

}
