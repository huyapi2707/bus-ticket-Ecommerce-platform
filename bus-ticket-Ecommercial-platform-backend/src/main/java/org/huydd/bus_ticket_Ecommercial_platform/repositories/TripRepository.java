package org.huydd.bus_ticket_Ecommercial_platform.repositories;

import org.huydd.bus_ticket_Ecommercial_platform.pojo.Trip;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends FilterAndPaginateRepository<Trip, Long> {
    List<Trip> findAllByRouteIdAndIsActive(Long routeId, boolean isActive);

}
