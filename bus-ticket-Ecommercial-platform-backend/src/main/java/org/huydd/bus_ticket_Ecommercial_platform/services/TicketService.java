package org.huydd.bus_ticket_Ecommercial_platform.services;

import org.huydd.bus_ticket_Ecommercial_platform.dtos.RouteDTO;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.SeatDTO;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.TicketDTO;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.TripDTO;
import org.huydd.bus_ticket_Ecommercial_platform.exceptions.IdNotFoundException;
import org.huydd.bus_ticket_Ecommercial_platform.exceptions.NoPermissionException;
import org.huydd.bus_ticket_Ecommercial_platform.mappers.TicketDTOMapper;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.*;
import org.huydd.bus_ticket_Ecommercial_platform.repositories.TicketRepository;
import org.huydd.bus_ticket_Ecommercial_platform.requestModels.TicketRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketService extends AbstractPaginateAndFilterService {

    private final RouteService routeService;
    private final TripService tripService;

    private final PaymentMethodService paymentMethodService;
    private final TicketRepository ticketRepository;


    private final TripSeatInfoService tripSeatInfoService;

    private final TicketStatusService ticketStatusService;

    private final TicketDTOMapper ticketDTOMapper;

    private final ConfigurationService configurationService;


    public TicketService(TicketRepository repository,
                         TicketDTOMapper dtoMapper,
                         RouteService routeService,
                         TripService tripService,
                         PaymentMethodService paymentMethodService,
                         TicketRepository ticketRepository,
                         TripSeatInfoService tripSeatInfoService,
                         TicketStatusService ticketStatusService,
                         TicketDTOMapper ticketDTOMapper,
                         ConfigurationService configurationService) {
        super(repository, dtoMapper);
        this.routeService = routeService;
        this.tripService = tripService;
        this.paymentMethodService = paymentMethodService;
        this.ticketRepository = ticketRepository;
        this.tripSeatInfoService = tripSeatInfoService;
        this.ticketStatusService = ticketStatusService;
        this.ticketDTOMapper = ticketDTOMapper;
        this.configurationService = configurationService;
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
                    .pickUpAddress(data.getPickUpPointAddress())
                    .seatInfo(thisSeat)
                    .seatPrice(thisRoute.getSeatPrice())
                    .luggage(Double.parseDouble(data.getSeatInfo().get("luggage").toString()))
                    .build();
        }).collect(Collectors.toList());
    }


    public List<Ticket> checkout(List<TicketRequest> cart, Long paymentMethodId)  {
        return createTickets(cart, paymentMethodId, null);
    }

    public TicketDTO handleCancelTicket(Long ticketId) {
        Ticket ticket = cancelTicket(ticketId);
        return (TicketDTO) toDTO(ticket);
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


    public List<Ticket> createTickets(List<TicketRequest> cart, Long paymentMethodId, User user) {
        List<Ticket> tickets = new ArrayList<>();
        PaymentMethod paymentMethod = paymentMethodService.getById(paymentMethodId);
        User finalUser = user != null ? user : (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TicketStatus ticketStatus = ticketStatusService.getUnpaidStatus();
        Integer maxCarryOnLuggage = configurationService.getMaxCarryOnCargoKg();
        cart.forEach(c -> {
            if (maxCarryOnLuggage.equals(c.getSeatInfo().get("luggage")))
                throw new IllegalArgumentException(String.format("Khối lượng hành lý mang theo phải ít hơn %d", maxCarryOnLuggage.intValue()));
            Long tripId = c.getTripId();
            Long seatId = c.getSeatInfo().get("id");

            SeatDTO thisSeat = tripSeatInfoService.handleNewTicket(tripId, seatId);

            Trip thisTrip = (Trip) tripService.getById(tripId);
            Ticket thisTicket = Ticket.builder()
                    .pickUpAddress(c.getPickUpPointAddress())
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

    public void saveAll(List<Ticket> tickets) {
        ticketRepository.saveAll(tickets);
    }

    public void addOnlinePaymentResult(Ticket ticket, OnlinePaymentResult onlinePaymentResult) {
        List<OnlinePaymentResult> results = ticket.getOnlinePaymentResults();
        if (results == null) {
            results = new ArrayList<>();
        }
        results.add(onlinePaymentResult);
        ticket.setOnlinePaymentResults(results);
        ticketRepository.save(ticket);
    }

    public void addOnlinePaymentResult(List<Ticket> tickets, OnlinePaymentResult onlinePaymentResult) {
        tickets.forEach(ticket -> {
            List<OnlinePaymentResult> results = ticket.getOnlinePaymentResults();
            if (results == null) {
                results = new ArrayList<>();
            }
            results.add(onlinePaymentResult);
            ticket.setOnlinePaymentResults(results);
        });
        ticketRepository.saveAll(tickets);
    }

    private boolean checkPermission(Ticket ticket) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (ticket.getCustomer().getId().equals(user.getId())) {
            return true;
        }
        return false;
    }

}
