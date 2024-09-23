package org.huydd.bus_ticket_Ecommercial_platform.pojo;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.Collection;


@Entity
@Table(name = "bus_station_route", schema = "busdb", catalog = "")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Route {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    @Basic
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    @Basic
    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Timestamp createdAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", referencedColumnName = "id", nullable = false)
    private BusCompany company;
    @ManyToOne
    @JoinColumn(name = "from_station_id", referencedColumnName = "id", nullable = false)
    private Station fromStation;
    @ManyToOne
    @JoinColumn(name = "to_station_id", referencedColumnName = "id", nullable = false)
    private Station toStation;
    @ManyToMany
    @JoinTable(
            name = "bus_station_route_pick_up_points",
            joinColumns = @JoinColumn(name = "route_id"),
            inverseJoinColumns = @JoinColumn(name = "station_id")

    )
    private Collection<Station> pickUpPoints;

    @Basic
    @Column(name = "seat_price", nullable = false, precision = 0)
    private Double seatPrice;

    @OneToMany(mappedBy = "route", fetch = FetchType.LAZY)
    private Collection<Trip> trips;

    @PostConstruct
    private void postConstruct() {
        this.setIsActive(true);
    }

}
