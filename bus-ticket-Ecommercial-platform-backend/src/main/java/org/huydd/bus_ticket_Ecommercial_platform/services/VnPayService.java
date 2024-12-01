package org.huydd.bus_ticket_Ecommercial_platform.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.OnlinePaymentResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:vnPay.properties")
public class VnPayService {

    @Value("${vnpayPayCommand}")
    private String vnpayPayCommand;

    @Value("${vnpayQueryCommand}")
    private String vnpayQueryCommand;

    @Value("${orderType}")
    private String orderType;

    @Value("${tmnCode}")
    private String tmnCode;

    @Value("${vnpayCurrCode}")
    private String vnpayCurrCode;

    @Value("${vnpayLocale}")
    private String vnpayLocale;

    @Value("${returnUrl}")
    private String returnUrl;

    @Value("${paymentUrl}")
    private String paymentUrl;

    @Value("${queryUrl}")
    private String queryUrl;

    @Value("${hashSecureKey}")
    private String hashSecureKey;

    @Value("${vnpVersion}")
    private String vnpVersion;

    private final String vnpOrderInfo = "Thanh toán vé xe";

    private final String successCode = "00";


    private final Map<String, String> payResponseMessage = new HashMap<>() {
        {
            put("00", "Giao dịch thành công.");
            put("07", "Trừ tiền thành công. Giao dịch bị nghi ngờ (liên quan tới lừa đảo, giao dịch bất thường).");
            put("09", "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng chưa đăng ký dịch vụ InternetBanking tại ngân hàng.");
            put("10", "Giao dịch không thành công do: Khách hàng xác thực thông tin thẻ/tài khoản không đúng quá 3 lần.");
            put("11", "Giao dịch không thành công do: Đã hết hạn chờ thanh toán. Xin quý khách vui lòng thực hiện lại giao dịch.");
            put("12", "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng bị khóa.");
            put("13", "Giao dịch không thành công do Quý khách nhập sai mật khẩu xác thực giao dịch (OTP). Xin quý khách vui lòng thực hiện lại giao dịch.");
            put("24", "Giao dịch không thành công do: Khách hàng hủy giao dịch");
            put("51", "Giao dịch không thành công do: Tài khoản của quý khách không đủ số dư để thực hiện giao dịch.");
            put("65", "Giao dịch không thành công do: Tài khoản của Quý khách đã vượt quá hạn mức giao dịch trong ngày.");
            put("75", "Ngân hàng thanh toán đang bảo trì.");
            put("79", "Giao dịch không thành công do: KH nhập sai mật khẩu thanh toán quá số lần quy định. Xin quý khách vui lòng thực hiện lại giao dịch.");
            put("99", "Các lỗi khác (lỗi còn lại, không có trong danh sách mã lỗi đã liệt kê).");
        }
    };

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

    private String hashAllFields(Map<String, String> fields) {
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);
        StringBuilder sb = new StringBuilder();
        fieldNames.forEach(fieldName -> {
            String fieldValue = fields.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                sb.append(fieldName);
                sb.append("=");
                sb.append(fieldValue);
            }
            if (fieldNames.indexOf(fieldName) == fieldNames.size() - 1) {
                sb.append("&");
            }
        });
        return hmacSHA512(hashSecureKey, sb.toString());
    }

    public String createVnPayPaymentUrl(OnlinePaymentResult result, String ip) {

        String vnpTxnRef = result.getPaymentCode();
        Double amount = result.getAmount();

        Timestamp createdTimestamp = result.getCreatedAt();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnpCreateDate = formatter.format(createdTimestamp);
        String vnpExpiredData = formatter.format(createdTimestamp.getTime() + TimeUnit.MINUTES.toMillis(15));


        Map<String, String> params = new HashMap<>();

        params.put("vnp_Command", vnpayPayCommand);
        params.put("vnp_OrderType", orderType);
        params.put("vnp_TmnCode", tmnCode);
        params.put("vnp_CurrCode", vnpayCurrCode);
        params.put("vnp_Locale", vnpayLocale);
        params.put("vnp_ReturnUrl", returnUrl);
        params.put("vnp_OrderInfo", vnpOrderInfo);


        params.put("vnp_CreateDate", vnpCreateDate);
        params.put("vnp_ExpireDate", vnpExpiredData);
        params.put("vnp_TxnRef", String.valueOf(vnpTxnRef));
        params.put("vnp_Amount", String.valueOf(amount.intValue() * 100));
        params.put("vnp_IpAddr", ip);
        params.put("vnp_Version", vnpVersion);
        List<String> fieldsName = new ArrayList<>(params.keySet());
        Collections.sort(fieldsName);
        Iterator<String> iterator = fieldsName.iterator();
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        while (iterator.hasNext()) {
            String fieldName = iterator.next();
            String fieldValue = params.get(fieldName);
            // Build hash data
            hashData.append(fieldName);
            hashData.append('=');
            hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
            //Build query
            query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
            query.append('=');
            query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
            if (iterator.hasNext()) {
                query.append('&');
                hashData.append('&');
            }
        }
        String queryUrl = query.toString();
        String vnpSecureHash = hmacSHA512(hashSecureKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;

        return paymentUrl + "?" + queryUrl;
    }


    public boolean isValidChecksum(Map<String, String> result) {
        Map<String, String> fields = new HashMap<>();

        for (Map.Entry<String, String> entry : result.entrySet()) {
            String fieldName = URLEncoder.encode(entry.getKey(), StandardCharsets.US_ASCII);
            String fieldValue = URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = result.get("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");
        String checksumValue = hashAllFields(fields);
        return checksumValue.equals(vnp_SecureHash);
    }

    public OnlinePaymentResult bindPaymentResult(OnlinePaymentResult paymentResult, Map<String, String> result) throws ParseException {
        String vnpTnxRef = result.get("vnp_TxnRef");

        if (!vnpTnxRef.equals(paymentResult.getPaymentCode())) {
            throw new IllegalArgumentException("vnp_TxnRef is not the same as paymentCode");
        }

        // common fields
        String vnpTransactionStatus = result.get("vnp_TransactionStatus");
        String vnpTransactionNo = result.get("vnp_TransactionNo");
        String vnpBankCode = result.get("vnp_BankCode");
        Double amount = Double.valueOf(result.get("vnp_Amount"));

        // common fields -> confirmAt
        String vnp_PayDate = result.get("vnp_PayDate");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date paidDate = formatter.parse(vnp_PayDate);
        Timestamp paidDateTimestamp = new Timestamp(paidDate.getTime());

        // optional fields
        String bankTransactionNo = "";
        String vnpCardType = "";
        if (result.containsKey("vnp_BankTranNo")) {
            bankTransactionNo = result.get("vnp_BankTranNo");
        }
        if (result.containsKey("vnp_CardType")) {
            bankTransactionNo = result.get("vnp_CardType");
        }

        paymentResult.setBankCode(vnpBankCode);
        paymentResult.setTransactionNo(vnpTransactionNo);
        paymentResult.setBankTransactionNo(bankTransactionNo);
        paymentResult.setCardType(vnpCardType);
        paymentResult.setConfirmAt(paidDateTimestamp);
        paymentResult.setAmount(amount);
        paymentResult.setIsSuccess(true);
        paymentResult.setTransactionCode(vnpTransactionStatus);
        paymentResult.setIsSuccess(vnpTransactionStatus.equals(successCode));
        paymentResult.setMessage(payResponseMessage.get("vnpTransactionStatus"));

        return paymentResult;
    }

    public OnlinePaymentResult fetchPaymentResult(OnlinePaymentResult onlinePaymentResult) throws ParseException, IOException, InterruptedException {
        JsonObject vnPayParams = new JsonObject();

        String vnpRequestId = String.valueOf(System.currentTimeMillis());
        vnPayParams.addProperty("vnp_RequestId", vnpRequestId);

        vnPayParams.addProperty("vnp_Version", vnpVersion);
        vnPayParams.addProperty("vnp_Command", vnpayQueryCommand);
        vnPayParams.addProperty("vnp_TmnCode", tmnCode);
        vnPayParams.addProperty("vnp_TxnRef", onlinePaymentResult.getPaymentCode());
        vnPayParams.addProperty("vnp_OrderInfo", vnpOrderInfo);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnpTransactionDate = formatter.format(onlinePaymentResult.getCreatedAt());

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        String vnpCreateDate = formatter.format(calendar.getTime());

        String vnpIpAddr = InetAddress.getLocalHost().getHostAddress();


        vnPayParams.addProperty("vnp_TransactionDate", vnpTransactionDate);
        vnPayParams.addProperty("vnp_CreateDate", vnpCreateDate);
        vnPayParams.addProperty("vnp_IpAddr", vnpIpAddr);

        String hashData = String.join("|",
                vnpRequestId,
                vnpVersion,
                vnpayQueryCommand,
                tmnCode,
                onlinePaymentResult.getPaymentCode(),
                vnpTransactionDate,
                vnpCreateDate,
                vnpIpAddr,
                vnpOrderInfo);
        String vnpSecureHash = hmacSHA512(hashSecureKey, hashData);
        vnPayParams.addProperty("vnp_SecureHash", vnpSecureHash);

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(queryUrl))
                    .POST(HttpRequest.BodyPublishers.ofString(vnPayParams.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IllegalArgumentException(response.body());
            }
            JsonObject jsonResult = JsonParser.parseString(response.body()).getAsJsonObject();
            Map mapResult = new Gson().fromJson(jsonResult, HashMap.class);
            onlinePaymentResult = bindPaymentResult(onlinePaymentResult, mapResult);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return onlinePaymentResult;    }
}
