package org.huydd.bus_ticket_Ecommercial_platform.responseModels;

import lombok.Builder;
import lombok.Data;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.UserDTO;

import java.io.Serializable;

@Data
@Builder
public class AuthenticationResponse implements Serializable {
    private String accessToken;
    private UserDTO userDetails;
}
