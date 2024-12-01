package org.huydd.bus_ticket_Ecommercial_platform.repositories;

import org.huydd.bus_ticket_Ecommercial_platform.pojo.BusCompany;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusCompanyRepository extends FilterAndPaginateRepository<BusCompany, Long> {
    Optional<BusCompany> findByManagerId(Long mangerId);
}
