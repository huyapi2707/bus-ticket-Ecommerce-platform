package org.huydd.bus_ticket_Ecommercial_platform.responseObjects;

import lombok.Builder;
import lombok.Data;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.TicketDTO;

import java.util.List;

@Data
@Builder
public class CheckoutResponse {
    private String paymentUrl;
    private List<TicketDTO> tickets;
}
