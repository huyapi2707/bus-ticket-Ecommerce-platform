package org.huydd.bus_ticket_Ecommercial_platform.services;

import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.PaymentMethod;
import org.huydd.bus_ticket_Ecommercial_platform.repositories.PaymentMethodRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;

    public List<PaymentMethod> getAll() {
        return paymentMethodRepository.findAllByIsActive(true);
    }

    public PaymentMethod getById(Long id) {
        return paymentMethodRepository.findById(id).get();
    }
}
