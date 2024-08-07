package org.huydd.bus_ticket_Ecommercial_platform.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;


@Entity
@Table(name = "bus_station_site", schema = "busdb", catalog = "")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Site {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    @OneToMany(mappedBy = "site")
    private Collection<Station> stations;

}
