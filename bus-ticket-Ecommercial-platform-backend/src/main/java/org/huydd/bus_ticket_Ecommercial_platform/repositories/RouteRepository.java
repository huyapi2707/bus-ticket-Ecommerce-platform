package org.huydd.bus_ticket_Ecommercial_platform.repositories;

import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.Route;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository

public interface RouteRepository extends FilterAndPaginateRepository<Route, Long> {
    List<Route> findAllByCompanyIdAndIsActive(Long BusCompanyId, boolean isActive);

}
