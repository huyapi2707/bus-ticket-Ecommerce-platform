package org.huydd.bus_ticket_Ecommercial_platform.requestObjects;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String role;
}
