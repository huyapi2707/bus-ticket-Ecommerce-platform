package org.huydd.bus_ticket_Ecommercial_platform.repositories;

import org.huydd.bus_ticket_Ecommercial_platform.pojo.Ticket;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends FilterAndPaginateRepository<Ticket, Long> {
}
