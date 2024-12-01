package org.huydd.bus_ticket_Ecommercial_platform.responseModels;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerPaymentResultResponse {
    private boolean isSuccess;
}
