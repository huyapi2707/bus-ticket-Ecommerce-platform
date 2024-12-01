package org.huydd.bus_ticket_Ecommercial_platform.services;

import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.OnlinePaymentResult;
import org.huydd.bus_ticket_Ecommercial_platform.repositories.OnlinePaymentResultRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OnlinePaymentResultService {
    private final OnlinePaymentResultRepository onlinePaymentResultRepository;

    public void saveOnlinePayment(OnlinePaymentResult onlinePaymentResult) {
        onlinePaymentResultRepository.save(onlinePaymentResult);
    }

    public void updateOnlinePayment(OnlinePaymentResult onlinePaymentResults) {
        onlinePaymentResultRepository.save(onlinePaymentResults);
    }

    public OnlinePaymentResult getByPaymentCode(String paymentCode) {
        Optional<OnlinePaymentResult> optionalResult = onlinePaymentResultRepository.findByPaymentCode(paymentCode);
        if (optionalResult.isEmpty()) {
            throw new IllegalArgumentException("Invalid payment code");
        }
        return optionalResult.get();
    }

    public OnlinePaymentResult createOnlinePayment() {
        String paymentCode = String.valueOf(new Date().getTime());
        OnlinePaymentResult paymentResult = OnlinePaymentResult.builder()
                .paymentCode(paymentCode)
                .build();
        onlinePaymentResultRepository.save(paymentResult);
        return paymentResult;
    }
}
