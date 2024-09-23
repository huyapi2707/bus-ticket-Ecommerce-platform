package org.huydd.bus_ticket_Ecommercial_platform.repositories;

import org.huydd.bus_ticket_Ecommercial_platform.pojo.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketStatusRepository extends JpaRepository<TicketStatus, Long> {
    Optional<TicketStatus> findByName(String name);
}
