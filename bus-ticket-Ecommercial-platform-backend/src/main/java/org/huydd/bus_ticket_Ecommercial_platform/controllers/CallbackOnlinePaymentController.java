package org.huydd.bus_ticket_Ecommercial_platform.controllers;

import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.responseModels.VnPayPaymentResponse;
import org.huydd.bus_ticket_Ecommercial_platform.services.PaymentService;
import org.huydd.bus_ticket_Ecommercial_platform.services.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.util.Map;


@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class CallbackOnlinePaymentController {

    private final PaymentService paymentService;
    private final TicketService ticketService;

    @GetMapping("/IPN")
    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED)
    public ResponseEntity<VnPayPaymentResponse> paymentResult(@RequestParam Map<String, String> result) throws ParseException {
        VnPayPaymentResponse response = paymentService.handleVnPayPaymentResult(result);
        return ResponseEntity.ok(response);
    }
}
