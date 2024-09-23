package org.huydd.bus_ticket_Ecommercial_platform.services;

import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.OnlinePaymentResult;
import org.huydd.bus_ticket_Ecommercial_platform.repositories.OnlinePaymentResultRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OnlinePaymentResultService {
    private final OnlinePaymentResultRepository onlinePaymentResultRepository;

    public void save(OnlinePaymentResult onlinePaymentResult) {
        onlinePaymentResultRepository.save(onlinePaymentResult);
    }

    public void update(OnlinePaymentResult onlinePaymentResults) {
        onlinePaymentResultRepository.save(onlinePaymentResults);
    }

    public OnlinePaymentResult getByPaymentCode(String paymentCode) {
        return onlinePaymentResultRepository.findByPaymentCode(paymentCode).get();
    }
}
