package org.huydd.bus_ticket_Ecommercial_platform.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "online_payment_result")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OnlinePaymentResult implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_code")
    private String paymentCode;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "confirm_at")
    private Timestamp confirmAt;

    @Column(name = "bank_code")
    private String bankCode;

    @Column(name = "transaction_no")
    private String transactionNo;

    @Column(name = "bank_transaction_no")
    private String bankTransactionNo;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "card_type")
    private String cardType;

    @Column(name = "is_success")
    private Boolean isSuccess;

    @Column(name = "transaction_code")
    private String transactionCode;

    @Column(name = "message")
    private String message;

    @ManyToMany(mappedBy = "onlinePaymentResults", fetch = FetchType.LAZY)
    private List<Ticket> tickets = new ArrayList<Ticket>();
}
