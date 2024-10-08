package org.huydd.bus_ticket_Ecommercial_platform.pojo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;


@Entity
@Table(name = "bus_station_user", schema = "busdb", catalog = "")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User implements UserDetails, Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "username", nullable = false, length = 255)
    private String username;
    @Basic
    @Column(name = "first_name", length = 255)
    private String firstName;
    @Basic
    @Column(name = "last_name", length = 255)
    private String lastName;
    @Basic
    @Column(name = "password", nullable = false, length = 255)
    private String password;
    @Basic
    @Column(name = "email", nullable = false, length = 254)
    private String email;
    @Basic
    @Column(name = "phone", length = 50)
    private String phone;
    @Basic
    @Column(name = "avatar", length = 255)
    private String avatar;
    @Basic
    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Timestamp createdAt;
    @Basic
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @OneToOne(mappedBy = "manager", cascade = {CascadeType.DETACH}, fetch = FetchType.LAZY)
    private BusCompany managed;
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private Collection<Ticket> tickets;
    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(this.getRole().getName());
        return List.of(authority);
    }

    @Override
    public boolean isEnabled() {
        return this.isActive;
    }
}
