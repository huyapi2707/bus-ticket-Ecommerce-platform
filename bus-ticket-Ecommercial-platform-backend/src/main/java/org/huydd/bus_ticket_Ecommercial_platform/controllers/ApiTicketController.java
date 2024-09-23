package org.huydd.bus_ticket_Ecommercial_platform.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.requestObjects.TicketRequest;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.TicketDTO;
import org.huydd.bus_ticket_Ecommercial_platform.services.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class ApiTicketController {

    private final TicketService ticketService;

    @PostMapping("/cart")
    public ResponseEntity<List<TicketDTO>> cart(@RequestBody List<TicketRequest> payload) {
        return ResponseEntity.ok(ticketService.handleCartInfo(payload));
    }

    @PostMapping("/")
    @Transactional(rollbackFor = RuntimeException.class)
    public ResponseEntity<Object> handleCheckout(@RequestBody List<TicketRequest> cart, @RequestParam(name = "paymentMethodId") Long paymentMethodId, HttpServletRequest request) throws UnsupportedEncodingException {
        String ip = request.getRemoteAddr();
        return ResponseEntity.ok(ticketService.checkout(cart, paymentMethodId, ip));
    }

    @DeleteMapping("/{id}")
    @Transactional(rollbackFor = RuntimeException.class)
    public ResponseEntity<TicketDTO> handleCancelTicket(@PathVariable Long id) {
        return new ResponseEntity<>(ticketService.handleCancelTicket(id), HttpStatus.NO_CONTENT);
    }
}
