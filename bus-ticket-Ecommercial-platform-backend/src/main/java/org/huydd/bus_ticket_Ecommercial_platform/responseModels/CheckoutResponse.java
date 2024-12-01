package org.huydd.bus_ticket_Ecommercial_platform.responseModels;

import lombok.Builder;
import lombok.Data;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.TicketDTO;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class CheckoutResponse implements Serializable {
    private String paymentUrl;
    private List<TicketDTO> tickets;
}
