package org.huydd.bus_ticket_Ecommercial_platform.services;

import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.OnlinePaymentResult;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.Ticket;
import org.huydd.bus_ticket_Ecommercial_platform.repositories.TicketRepository;
import org.huydd.bus_ticket_Ecommercial_platform.repositories.TicketStatusRepository;
import org.huydd.bus_ticket_Ecommercial_platform.responseObjects.OnlinePaymentResponse;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:vnPay.properties")
public class PaymentService {

    private final OnlinePaymentResultService onlinePaymentResultService;

    private final Environment environment;

    private final TicketRepository ticketRepository;

    public String createVnPayPaymentUrl(List<Ticket> tickets, String ip) throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<>();

        params.put("vnp_Command", environment.getProperty("vnpayCommand"));
        params.put("vnp_OrderType", environment.getProperty("orderType"));
        params.put("vnp_TmnCode", environment.getProperty("tmnCode"));
        params.put("vnp_CurrCode", environment.getProperty("vnpayCurrCode"));
        params.put("vnp_Locale", environment.getProperty("vnpayLocale"));
        params.put("vnp_ReturnUrl", environment.getProperty("returnUrl"));
        params.put("vnp_OrderInfo", "Pay for bus ticket");
        String vnpPaymentUrl = environment.getProperty("paymentUrl");
        String vnpHashSecureKey = environment.getProperty("hashSecureKey");
        long vnpTxnRef = new Date().getTime();
        long amount = 0;
        OnlinePaymentResult paymentResults = OnlinePaymentResult.builder()
                .paymentCode(String.valueOf(vnpTxnRef))
                .build();
        onlinePaymentResultService.save(paymentResults);
        for (Ticket ticket : tickets) {
            amount += ticket.getSeatPrice();
            ticket.setOnlinePaymentResult(paymentResults);
        }
        ticketRepository.saveAll(tickets);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnpCreateDate = formatter.format(calendar.getTime());
        calendar.add(Calendar.MINUTE, 15);
        String vnpExpiredData = formatter.format(calendar.getTime());
        params.put("vnp_CreateDate", vnpCreateDate);
        params.put("vnp_ExpireDate", vnpExpiredData);
        params.put("vnp_TxnRef", String.valueOf(vnpTxnRef));
        params.put("vnp_Amount", String.valueOf(amount * 100));
        params.put("vnp_IpAddr", ip);
        params.put("vnp_Version", environment.getProperty("vnpVersion"));
        List<String> fieldsName = new ArrayList<>(params.keySet());
        Collections.sort(fieldsName);
        Iterator iterator = fieldsName.iterator();
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        while (iterator.hasNext()) {
            String fieldName = iterator.next().toString();
            String fieldValue = params.get(fieldName);
            // Build hash data
            hashData.append(fieldName);
            hashData.append('=');
            hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
            //Build query
            query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
            query.append('=');
            query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
            if (iterator.hasNext()) {
                query.append('&');
                hashData.append('&');
            }
        }
        String queryUrl = query.toString();
        String vnpSecureHash = hmacSHA512(vnpHashSecureKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;


        return vnpPaymentUrl + "?" + queryUrl;
    }


    private String hmacSHA512(String key, String data) {
        try {

            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (Exception ex) {
            return "";
        }
    }

    public OnlinePaymentResponse handleVnPayPaymentResult(Map<String, String> result) throws ParseException {
        String vnpTnxRef = result.get("vnp_TxnRef");
        String vnpTransactionNo = result.get("vnp_TransactionNo");
        String bankTransactionNo = result.get("vnp_BankTranNo");
        String vnpBankCode = result.get("vnp_BankCode");
        String vnpTransactionStatus = result.get("vnp_TransactionStatus");
        String vnp_PayDate = result.get("vnp_PayDate");
        String vnpCardType = result.get("vnp_CardType");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = formatter.parse(vnp_PayDate);
        Timestamp timestamp = new Timestamp(date.getTime());
        if (vnpTransactionStatus.equals("00")) {
            OnlinePaymentResult paymentResult = onlinePaymentResultService.getByPaymentCode(vnpTnxRef);
            paymentResult.setBankCode(vnpBankCode);
            paymentResult.setTransactionNo(vnpTransactionNo);
            paymentResult.setBankTransactionNo(bankTransactionNo);
            paymentResult.setCardType(vnpCardType);
            paymentResult.setConfirmAt(timestamp);
            List<Ticket> tickets = paymentResult.getTickets();
            for (Ticket ticket : tickets) {
                ticket.setPaidAt(timestamp);
            }
            ticketRepository
                    .saveAll(tickets);
            onlinePaymentResultService.update(paymentResult);
            return OnlinePaymentResponse.builder()
                    .isSuccess(true)
                    .message("Bạn đã thanh toán thành công")
                    .build();
        }
        return OnlinePaymentResponse.builder()
                .isSuccess(false)
                .message("Thanh toán thất bại")
                .build();
    }
}
