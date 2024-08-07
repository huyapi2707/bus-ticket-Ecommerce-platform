package org.huydd.bus_ticket_Ecommercial_platform.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.Collection;


@Entity
@Table(name = "bus_station_ticket", schema = "busdb", catalog = "")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Timestamp createdAt;
    @Basic
    @Column(name = "seat_price", nullable = false, precision = 0)
    private Double seatPrice;
    @Basic
    @Column(name = "seat_code", nullable = false, length = 20)
    private String seatCode;
    @Basic
    @Column(name = "paid_at", nullable = false)
    private Timestamp paidAt;

    @OneToMany(mappedBy = "ticket")
    private Collection<Review> reviews;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    private User customer;
    @ManyToOne
    @JoinColumn(name = "payment_method_id", referencedColumnName = "id", nullable = false)
    private PaymentMethod paymentMethod;
    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id", nullable = false)
    private TicketStatus status;
    @ManyToOne
    @JoinColumn(name = "trip_id", referencedColumnName = "id", nullable = false)
    private Trip trip;

}
