package org.huydd.bus_ticket_Ecommercial_platform.controllers;

import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.RouteDTO;
import org.huydd.bus_ticket_Ecommercial_platform.services.RouteService;
import org.huydd.bus_ticket_Ecommercial_platform.specifications.RouteSpecification;
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
        return ResponseEntity.ok(routeService.getAllAndFilter(params, RouteSpecification.class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteDTO> retrieve(@PathVariable Long id) {
        return ResponseEntity.ok((RouteDTO) routeService.toDTO(routeService.getById(id)));
    }

    @GetMapping("/{id}/trips")
    public ResponseEntity<Object> trips(@PathVariable Long id)  {
        return ResponseEntity.ok(routeService.getTrips(id));
    }
}
