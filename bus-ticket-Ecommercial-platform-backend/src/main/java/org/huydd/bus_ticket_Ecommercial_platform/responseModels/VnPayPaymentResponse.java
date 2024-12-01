package org.huydd.bus_ticket_Ecommercial_platform.responseModels;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class VnPayPaymentResponse implements Serializable {
    private String RspCode;
    private String Message;
}
