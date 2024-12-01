package org.huydd.bus_ticket_Ecommercial_platform.requestModels;

import lombok.Data;

import java.util.Map;

@Data
public class TicketRequest {
    private Long tripId;
    private Map<String, Long> seatInfo;
    private String pickUpPointAddress;
}
