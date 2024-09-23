package org.huydd.bus_ticket_Ecommercial_platform.controllers;

import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.TripSeatInfo;
import org.huydd.bus_ticket_Ecommercial_platform.services.TripService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/trips")
@RequiredArgsConstructor
public class ApiTripController {

    private final TripService tripService;

    @GetMapping("/{id}/seats")
    public ResponseEntity<TripSeatInfo> getSeatInfo(@PathVariable Long id) {
        return ResponseEntity.ok(tripService.getTripSeatInfoByTripId(id, true));
    }
}
