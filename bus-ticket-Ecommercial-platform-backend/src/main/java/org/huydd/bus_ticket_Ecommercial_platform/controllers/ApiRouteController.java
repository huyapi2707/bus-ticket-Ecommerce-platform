package org.huydd.bus_ticket_Ecommercial_platform.controllers;

import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.RouteDTO;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.TripDTO;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.Route;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.Trip;
import org.huydd.bus_ticket_Ecommercial_platform.services.RouteService;
import org.huydd.bus_ticket_Ecommercial_platform.services.TripService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/routes")
@RequiredArgsConstructor
public class ApiRouteController {

    private final RouteService routeService;

    private final TripService tripService;

    @GetMapping("/")
    public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        return ResponseEntity.ok(routeService.handleGetAllAndFilter(params,15));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteDTO> retrieve(@PathVariable Long id) {
        Route result = routeService.getById(id);
        return ResponseEntity.ok((RouteDTO) routeService.toDTO(result));
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam Map<String, String > params) {
        return ResponseEntity.ok(routeService.handleSearch(params));
    }

    @GetMapping("/{id}/trips")
    public ResponseEntity<List<TripDTO>> trips(@PathVariable Long id)  {
        List<Trip> results = routeService.getTrips(id);
        List<TripDTO> responseResults = results.stream()
                .map(tripService::toDTO)
                .map(trip -> (TripDTO) trip)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseResults);
    }
}
