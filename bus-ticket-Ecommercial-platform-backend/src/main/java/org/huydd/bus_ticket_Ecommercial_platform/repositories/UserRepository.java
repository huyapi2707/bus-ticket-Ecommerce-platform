package org.huydd.bus_ticket_Ecommercial_platform.repositories;


import org.huydd.bus_ticket_Ecommercial_platform.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> getUserByUsername(String username);
}
