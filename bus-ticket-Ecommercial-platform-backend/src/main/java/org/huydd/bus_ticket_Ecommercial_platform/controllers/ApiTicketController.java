package org.huydd.bus_ticket_Ecommercial_platform.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.TicketDTO;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.PaymentMethod;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.Ticket;
import org.huydd.bus_ticket_Ecommercial_platform.requestModels.TicketRequest;
import org.huydd.bus_ticket_Ecommercial_platform.responseModels.CheckoutResponse;
import org.huydd.bus_ticket_Ecommercial_platform.services.PaymentMethodService;
import org.huydd.bus_ticket_Ecommercial_platform.services.PaymentService;
import org.huydd.bus_ticket_Ecommercial_platform.services.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class ApiTicketController {

    private final TicketService ticketService;
    private final PaymentMethodService paymentMethodService;
    private final PaymentService paymentService;

    @PostMapping("/cart")
    public ResponseEntity<List<TicketDTO>> cart(@RequestBody List<TicketRequest> payload) {
        return ResponseEntity.ok(ticketService.handleCartInfo(payload));
    }

    @PostMapping("/")
    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED)
    public ResponseEntity<CheckoutResponse> handleCheckout(@RequestBody List<TicketRequest> cart, @RequestParam(name = "paymentMethodId") Long paymentMethodId, HttpServletRequest request) {

        List<Ticket> tickets = ticketService.checkout(cart, paymentMethodId);
        List<TicketDTO> ticketDTOList = tickets.stream()
                .map(ticketService::toDTO)
                .map(ticket -> (TicketDTO)ticket)
                .toList();
        PaymentMethod paymentMethod = paymentMethodService.getById(paymentMethodId);

        String paymentUrl = "";
        if (paymentMethod.getName().equals("VNPAY")) {
            String ip = request.getRemoteAddr();
            paymentUrl = paymentService.createOnlinePaymentUrl(tickets, ip);
        }

        CheckoutResponse checkoutResponse = CheckoutResponse.builder()
                .paymentUrl(paymentUrl)
                .tickets(ticketDTOList)
                .build();
        return ResponseEntity.ok(checkoutResponse);
    }

    @DeleteMapping("/{id}")
    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED)
    public ResponseEntity<TicketDTO> handleCancelTicket(@PathVariable Long id) {
        return new ResponseEntity<>(ticketService.handleCancelTicket(id), HttpStatus.NO_CONTENT);
    }
}
