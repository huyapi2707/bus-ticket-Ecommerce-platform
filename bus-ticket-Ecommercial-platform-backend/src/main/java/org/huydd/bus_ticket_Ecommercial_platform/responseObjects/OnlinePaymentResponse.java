package org.huydd.bus_ticket_Ecommercial_platform.responseObjects;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OnlinePaymentResponse {
    private Boolean isSuccess;
    private String message;
}
