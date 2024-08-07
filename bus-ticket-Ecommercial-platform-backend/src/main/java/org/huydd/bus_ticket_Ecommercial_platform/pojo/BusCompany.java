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
@Table(name = "bus_station_buscompany", schema = "busdb", catalog = "")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusCompany {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    @Basic
    @Column(name = "avatar", nullable = false, length = 255)
    private String avatar;
    @Basic
    @Column(name = "phone", nullable = false, length = 50)
    private String phone;
    @Basic
    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified;
    @Basic
    @Column(name = "operation_cost", nullable = false, precision = 0)
    private Double operationCost;
    @Basic
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    @Basic
    @Column(name = "email", nullable = false, length = 254)
    private String email;
    @Basic
    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Timestamp createdAt;
    @OneToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "id", nullable = false)
    private User manager;
    @OneToMany(mappedBy = "company")
    private Collection<Car> ownedCars;

    @OneToMany(mappedBy = "company")
    private Collection<Route> routes;

    @PostConstruct
    private void postConstruct() {
        this.setIsActive(false);
        this.setIsVerified(false);
    }

}
