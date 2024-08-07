package org.huydd.bus_ticket_Ecommercial_platform.pojo;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "bus_station_trip", schema = "busdb", catalog = "")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trip {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "seat_price", nullable = false, precision = 0)
    private Double seatPrice;
    @Basic
    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Timestamp createdAt;
    @Basic
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    @Basic
    @Column(name = "depart_at", nullable = false)
    private Timestamp departAt;

    @OneToMany(mappedBy = "trip")
    private Collection<Ticket> tickets;
    @ManyToOne
    @JoinColumn(name = "car_id", referencedColumnName = "id", nullable = false)
    private Car car;
    @ManyToOne
    @JoinColumn(name = "route_id", referencedColumnName = "id", nullable = false)
    private Route route;

    @PostConstruct
    private void postConstruct() {
        this.setIsActive(true);
    }
}
