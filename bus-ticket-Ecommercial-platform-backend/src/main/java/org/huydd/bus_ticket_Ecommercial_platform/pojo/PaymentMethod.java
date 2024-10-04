package org.huydd.bus_ticket_Ecommercial_platform.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Entity
@Table(name = "bus_station_paymentmethod", schema = "busdb", catalog = "")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethod implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name="is_active", nullable = false)
    private Boolean isActive;

    @Basic
    @Column(name = "name", nullable = false, length = 10)
    private String name;


}
