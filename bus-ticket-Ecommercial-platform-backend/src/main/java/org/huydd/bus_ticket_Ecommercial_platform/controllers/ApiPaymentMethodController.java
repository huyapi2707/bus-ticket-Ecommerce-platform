package org.huydd.bus_ticket_Ecommercial_platform.controllers;

import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.PaymentMethod;
import org.huydd.bus_ticket_Ecommercial_platform.services.PaymentMethodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payment_methods")
@RequiredArgsConstructor
public class ApiPaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    @GetMapping("/")
    public ResponseEntity<List<PaymentMethod>> list() {
        return ResponseEntity.ok(paymentMethodService.getAll());
    }
}

