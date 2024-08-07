package org.huydd.bus_ticket_Ecommercial_platform.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "bus_station_role", schema = "busdb", catalog = "")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Role {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "name", nullable = false, length = 50)
    private String name;

}
