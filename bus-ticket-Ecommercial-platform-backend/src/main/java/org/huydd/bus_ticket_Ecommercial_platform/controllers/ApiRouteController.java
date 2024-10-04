package org.huydd.bus_ticket_Ecommercial_platform.controllers;

import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.RouteDTO;
import org.huydd.bus_ticket_Ecommercial_platform.services.RouteService;
import org.huydd.bus_ticket_Ecommercial_platform.specifications.RouteSpecification;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/routes")
@RequiredArgsConstructor
public class ApiRouteController {

    private final RouteService routeService;

    @GetMapping("/")
    public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        return ResponseEntity.ok(routeService.handleGetAllAndFilter(params,15));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteDTO> retrieve(@PathVariable Long id) {
        return ResponseEntity.ok(routeService.getByIdToDto(id));
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam Map<String, String > params) {
        return ResponseEntity.ok(routeService.handleSearch(params));
    }

    @GetMapping("/{id}/trips")
    public ResponseEntity<Object> trips(@PathVariable Long id)  {
        return ResponseEntity.ok(routeService.getTrips(id));
    }
}
