package org.huydd.bus_ticket_Ecommercial_platform.requestObjects;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginWithGoogleRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String avatar;
}
