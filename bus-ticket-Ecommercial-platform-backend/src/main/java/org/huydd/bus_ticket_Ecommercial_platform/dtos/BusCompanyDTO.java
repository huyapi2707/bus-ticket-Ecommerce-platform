package org.huydd.bus_ticket_Ecommercial_platform.dtos;

import lombok.Builder;
import lombok.Data;


@Builder
public record BusCompanyDTO(
        Long id,
        String name,
        String avatar,
        String phone,
        String email,
        Boolean isVerified,
        Boolean isActive,
        Long managerId
) {
}
