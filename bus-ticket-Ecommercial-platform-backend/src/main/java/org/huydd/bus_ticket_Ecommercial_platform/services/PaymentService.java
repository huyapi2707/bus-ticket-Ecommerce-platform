package org.huydd.bus_ticket_Ecommercial_platform.services;

import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.OnlinePaymentResult;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.Ticket;
import org.huydd.bus_ticket_Ecommercial_platform.responseModels.CustomerPaymentResultResponse;
import org.huydd.bus_ticket_Ecommercial_platform.responseModels.VnPayPaymentResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OnlinePaymentResultService onlinePaymentResultService;
    private final VnPayService vnPayService;
    private final OnlinePaymentResultService paymentResultService;
    private final TicketService ticketService;


    public String createOnlinePaymentUrl(List<Ticket> tickets, String ip) {
        OnlinePaymentResult paymentResult = paymentResultService.createOnlinePayment();
        double amount = 0;
        for (Ticket ticket : tickets) {
            amount += ticket.getSeatPrice();
            ticketService.addOnlinePaymentResult(ticket, paymentResult);
        }
        paymentResult.setAmount(amount);
        return vnPayService.createVnPayPaymentUrl(paymentResult, ip);

    }



    public VnPayPaymentResponse handleVnPayPaymentResult(Map<String, String> result) throws ParseException {

        if (!vnPayService.isValidChecksum(result)) {
            return VnPayPaymentResponse.builder()
                    .RspCode("01")
                    .Message("Invalid checksum")
                    .build();
        }
        String vnpTnxRef = result.get("vnp_TxnRef");
        OnlinePaymentResult paymentResult = onlinePaymentResultService.getByPaymentCode(vnpTnxRef);

        paymentResult = vnPayService.bindPaymentResult(paymentResult, result);

        List<Ticket> tickets = paymentResult.getTickets();
        for (Ticket ticket : tickets) {
            ticket.setPaidAt(paymentResult.getConfirmAt());
        }
        ticketService.saveAll(tickets);
        onlinePaymentResultService.updateOnlinePayment(paymentResult);

        return VnPayPaymentResponse.builder()
                .RspCode(paymentResult.getTransactionCode())
                .Message(paymentResult.getMessage())
                .build();
    }

    public CustomerPaymentResultResponse queryPaymentResult(String provider, Long userId, String paymentCode) throws ParseException, IOException, InterruptedException {
        OnlinePaymentResult result = onlinePaymentResultService.getByPaymentCode(paymentCode);
        if (result.getIsSuccess() != null) {
            return CustomerPaymentResultResponse.builder()
                    .isSuccess(result.getIsSuccess())
                    .build();
        }
        if (provider.equals("VNPAY")) {
            result = vnPayService.fetchPaymentResult(result);
            paymentResultService.saveOnlinePayment(result);
        }
        return CustomerPaymentResultResponse.builder()
                .isSuccess(result.getIsSuccess())
                .build();
    }
}
