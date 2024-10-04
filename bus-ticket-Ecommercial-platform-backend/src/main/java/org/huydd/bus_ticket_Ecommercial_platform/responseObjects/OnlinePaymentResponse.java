package org.huydd.bus_ticket_Ecommercial_platform.responseObjects;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class OnlinePaymentResponse implements Serializable {
    private Boolean isSuccess;
    private String message;
}
