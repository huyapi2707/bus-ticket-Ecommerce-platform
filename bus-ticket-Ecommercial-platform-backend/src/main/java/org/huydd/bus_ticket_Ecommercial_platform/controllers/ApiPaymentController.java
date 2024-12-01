package org.huydd.bus_ticket_Ecommercial_platform.controllers;

import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.PaymentMethod;
import org.huydd.bus_ticket_Ecommercial_platform.responseModels.CustomerPaymentResultResponse;
import org.huydd.bus_ticket_Ecommercial_platform.services.PaymentMethodService;
import org.huydd.bus_ticket_Ecommercial_platform.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class ApiPaymentController {

    private final PaymentMethodService paymentMethodService;
    private final PaymentService paymentService;

    @GetMapping("/")
    public ResponseEntity<List<PaymentMethod>> list() {
        return ResponseEntity.ok(paymentMethodService.getAll());
    }

    @GetMapping("/{provider}/result")
    public ResponseEntity<CustomerPaymentResultResponse> handleQueryPaymentResult(@PathVariable("provider") String provider, @RequestParam String paymentCode, @RequestParam Long userId) throws ParseException, IOException, InterruptedException {
        return ResponseEntity.ok(paymentService.queryPaymentResult(provider, userId, paymentCode));
    }
}

