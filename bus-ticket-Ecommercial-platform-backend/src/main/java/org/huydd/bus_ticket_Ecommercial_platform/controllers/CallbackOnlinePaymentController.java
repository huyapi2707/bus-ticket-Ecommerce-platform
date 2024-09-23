package org.huydd.bus_ticket_Ecommercial_platform.controllers;

import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.responseObjects.OnlinePaymentResponse;
import org.huydd.bus_ticket_Ecommercial_platform.services.PaymentService;
import org.huydd.bus_ticket_Ecommercial_platform.services.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import org.yaml.snakeyaml.util.UriEncoder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Map;


@Controller
@RequestMapping("/online_payment_result")
@RequiredArgsConstructor
public class CallbackOnlinePaymentController {
    private final PaymentService paymentService;

    @GetMapping("/vnpay")
    @Transactional(rollbackFor = RuntimeException.class)
    public RedirectView paymentResult(@RequestParam Map<String, String> result) throws ParseException, UnsupportedEncodingException, URISyntaxException {
        OnlinePaymentResponse response = paymentService.handleVnPayPaymentResult(result);
        String responseURLParameter = String.format("?payment_status=%b&payment_response=%s",
                URLEncoder.encode(response.getIsSuccess().toString(), "UTF-8"),
                URLEncoder.encode(response.getMessage(), "UTF-8"));

        return new RedirectView("http://localhost:3000/ticket" + responseURLParameter);
    }
}
