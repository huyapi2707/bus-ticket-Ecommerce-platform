package org.huydd.bus_ticket_Ecommercial_platform.services;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.TicketStatus;
import org.huydd.bus_ticket_Ecommercial_platform.repositories.TicketStatusRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Getter
public class TicketStatusService {
    private final String ABSENTED = "ABSENTED";

    private final String CANCELED = "CANCELED";

    private final String COMPLETED = "COMPLETED";

    private final String PAID = "PAID";

    private final String REFUNDED = "REFUNDED";

    private final String UNPAID = "UNPAID";


    private final TicketStatusRepository ticketStatusRepository;

    public TicketStatus getUnpaidStatus() {
        return ticketStatusRepository.findByName(UNPAID).get();
    }
    public TicketStatus getCanceledStatus() {
        return ticketStatusRepository.findByName(CANCELED).get();
    }

    public TicketStatus getStatusByName(String name) {
        Optional<TicketStatus> optionalTicketStatus = ticketStatusRepository.findByName(name);
        if (optionalTicketStatus.isEmpty()) throw new IllegalArgumentException("No ticket status name found");
        return optionalTicketStatus.get();
    }
}
