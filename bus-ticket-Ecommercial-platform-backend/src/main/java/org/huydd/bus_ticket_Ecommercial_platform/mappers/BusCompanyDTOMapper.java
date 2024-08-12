package org.huydd.bus_ticket_Ecommercial_platform.mappers;

import org.huydd.bus_ticket_Ecommercial_platform.dtos.BusCompanyDTO;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.BusCompany;
import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public class BusCompanyDTOMapper implements Function<BusCompany, BusCompanyDTO> {
    @Override
    public BusCompanyDTO apply(BusCompany busCompany) {
        return  BusCompanyDTO.builder()
                .id(busCompany.getId())
                .name(busCompany.getName())
                .avatar(busCompany.getAvatar())
                .isActive(busCompany.getIsActive())
                .email(busCompany.getEmail())
                .isVerified(busCompany.getIsVerified())
                .phone(busCompany.getPhone())
                .managerId(busCompany.getManager().getId())
                .build();
    }
}
