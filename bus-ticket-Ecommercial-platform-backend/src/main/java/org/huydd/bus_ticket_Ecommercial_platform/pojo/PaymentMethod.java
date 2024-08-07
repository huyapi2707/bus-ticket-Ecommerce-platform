package org.huydd.bus_ticket_Ecommercial_platform.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "bus_station_paymentmethod", schema = "busdb", catalog = "")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethod {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "name", nullable = false, length = 10)
    private String name;


}
