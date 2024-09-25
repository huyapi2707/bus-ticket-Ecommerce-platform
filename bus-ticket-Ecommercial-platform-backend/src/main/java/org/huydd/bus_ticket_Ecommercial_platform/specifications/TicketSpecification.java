package org.huydd.bus_ticket_Ecommercial_platform.specifications;

import org.huydd.bus_ticket_Ecommercial_platform.dtos.SearchCriteria;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.Ticket;

public class TicketSpecification extends AbstractSpecification<Ticket> {
    public TicketSpecification(SearchCriteria criteria) {
        super(criteria);
    }
}
