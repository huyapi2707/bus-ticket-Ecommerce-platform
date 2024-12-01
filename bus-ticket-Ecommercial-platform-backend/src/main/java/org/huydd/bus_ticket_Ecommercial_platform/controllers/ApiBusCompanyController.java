package org.huydd.bus_ticket_Ecommercial_platform.controllers;

import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.BusCompanyDTO;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.RouteDTO;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.BusCompany;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.Route;
import org.huydd.bus_ticket_Ecommercial_platform.services.BusCompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/bus_companies")
@RequiredArgsConstructor
public class ApiBusCompanyController {

    private final BusCompanyService busCompanyService;

    @GetMapping("/")
    public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return ResponseEntity.ok( busCompanyService.handleGetAllAndFilter(params, 15));
    }

    @PostMapping("/")
    public ResponseEntity<BusCompanyDTO> create(@RequestBody BusCompanyDTO payload) throws IOException {
        BusCompany result = busCompanyService.createCompany(payload);
        return ResponseEntity.ok((BusCompanyDTO) busCompanyService.toDTO(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusCompanyDTO> retrieve(@PathVariable Long id) {
        BusCompany result = busCompanyService.getById(id);
        return ResponseEntity.ok((BusCompanyDTO) busCompanyService.toDTO(result));
    }

    @GetMapping("/{id}/routes")
    public ResponseEntity<List<RouteDTO>> routes(@PathVariable Long id)  {
        List<Route> results = busCompanyService.getRoutes(id);
        List<RouteDTO> responseResults = results.stream()
                .map(busCompanyService::toDTO)
                .map(route -> (RouteDTO)route)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseResults);
    }
}
