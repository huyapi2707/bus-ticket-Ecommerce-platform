package org.huydd.bus_ticket_Ecommercial_platform.repositories;

import org.huydd.bus_ticket_Ecommercial_platform.pojo.Route;

import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends FilterAndPaginateRepository<Route, Long> {
}
