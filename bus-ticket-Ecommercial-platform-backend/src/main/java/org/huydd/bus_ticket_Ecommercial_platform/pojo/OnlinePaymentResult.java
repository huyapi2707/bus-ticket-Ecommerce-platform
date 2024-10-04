package org.huydd.bus_ticket_Ecommercial_platform.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.context.annotation.Bean;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
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
    @Column(name = "card_type")
    private String cardType;

    @OneToMany(mappedBy = "onlinePaymentResult")
    private List<Ticket> tickets;
}
