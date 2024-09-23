package org.huydd.bus_ticket_Ecommercial_platform.requestObjects;

import lombok.Data;

import java.util.Map;

@Data
public class TicketRequest {
    private Long tripId;
    private Map<String, Long> seatInfo;
}
