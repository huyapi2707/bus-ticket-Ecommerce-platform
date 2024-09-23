package org.huydd.bus_ticket_Ecommercial_platform.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SeatDTO {
    private Long id;
    private String code;
}
