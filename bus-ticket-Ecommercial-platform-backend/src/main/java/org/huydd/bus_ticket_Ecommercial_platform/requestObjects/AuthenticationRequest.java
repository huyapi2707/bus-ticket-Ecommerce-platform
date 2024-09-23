package org.huydd.bus_ticket_Ecommercial_platform.requestObjects;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationRequest {
    private String username;
    private String password;
}
