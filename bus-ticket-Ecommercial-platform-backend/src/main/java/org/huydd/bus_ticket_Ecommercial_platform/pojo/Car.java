package org.huydd.bus_ticket_Ecommercial_platform.pojo;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;


@Entity
@Table(name = "bus_station_car", schema = "busdb", catalog = "")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Car {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "car_number", nullable = false, length = 10)
    private String carNumber;


    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_owner_id", referencedColumnName = "id", nullable = false)
    private BusCompany company;
    @OneToMany(mappedBy = "car", cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private Collection<Seat> seats;

    @PostConstruct
    private void postConstruct() {
        this.setIsActive(true);
    }

}
