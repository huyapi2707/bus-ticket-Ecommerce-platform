package org.huydd.bus_ticket_Ecommercial_platform.pojo;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Entity
@Table(name = "bus_station_seat", schema = "busdb", catalog = "")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seat implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "code", nullable = false, length = 5)
    private String code;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "car_id", referencedColumnName = "id", nullable = false)
    private Car car;

    @PostConstruct
    private void postConstruct() {
        this.setIsActive(true);
    }

}
