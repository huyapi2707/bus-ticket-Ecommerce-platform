package org.huydd.bus_ticket_Ecommercial_platform.dtos;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;


@Builder
@Data
public class BusCompanyDTO implements Serializable {
    private Long id;
    private String name;
    private String avatar;
    private String phone;
    private String email;
    private Boolean isVerified;
    private Boolean isActive;
    private Long managerId;
}
