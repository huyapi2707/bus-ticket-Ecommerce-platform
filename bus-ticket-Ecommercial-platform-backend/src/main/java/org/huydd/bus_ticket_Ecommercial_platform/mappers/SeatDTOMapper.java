package org.huydd.bus_ticket_Ecommercial_platform.mappers;

import org.huydd.bus_ticket_Ecommercial_platform.dtos.SeatDTO;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.Seat;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class SeatDTOMapper implements Function<Seat, SeatDTO> {
    @Override
    public SeatDTO apply(Seat seat) {
        return SeatDTO.builder()
                .id(seat.getId())
                .code(seat.getCode())
                .build();
    }
}
