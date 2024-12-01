package org.huydd.bus_ticket_Ecommercial_platform.repositories;

import org.huydd.bus_ticket_Ecommercial_platform.pojo.TripSeatInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TripSeatInfoRepository extends MongoRepository<TripSeatInfo, String> {
    @Query(value = "{'tripId':  ?0, 'isActive': ?1}")
    Optional<TripSeatInfo> findTripSeatInfoByTripIdAndIsActive(Long tripId, Boolean isActive);



}
