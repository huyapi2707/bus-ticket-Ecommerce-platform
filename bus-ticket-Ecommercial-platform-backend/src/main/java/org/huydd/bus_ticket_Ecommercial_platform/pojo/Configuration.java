package org.huydd.bus_ticket_Ecommercial_platform.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Entity
@Table(name = "bus_station_configuration", schema = "busdb", catalog = "")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Configuration implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "default_operation_cost", nullable = false, precision = 0)
    private Double defaultOperationCost;
    @Basic
    @Column(name = "jwt_expiration_duration", nullable = false)
    private Integer jwtExpirationDuration;
    @Basic
    @Column(name = "max_carryOn_cargo_kg", nullable = false)
    private Integer maxCarryOnLuggageKg;

}
