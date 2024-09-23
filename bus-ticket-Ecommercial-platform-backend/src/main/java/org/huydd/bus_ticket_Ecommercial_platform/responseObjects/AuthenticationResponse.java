package org.huydd.bus_ticket_Ecommercial_platform.responseObjects;

import lombok.Builder;
import lombok.Data;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.UserDTO;

@Data
@Builder
public class AuthenticationResponse {
    private String accessToken;
    private UserDTO userDetails;
}
