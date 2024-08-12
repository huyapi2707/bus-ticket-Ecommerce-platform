package org.huydd.bus_ticket_Ecommercial_platform.controllers;

import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.BusCompanyDTO;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.BusCompany;
import org.huydd.bus_ticket_Ecommercial_platform.services.BusCompanyService;
import org.huydd.bus_ticket_Ecommercial_platform.specifications.BusCompanySpecification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/bus_company")
@RequiredArgsConstructor
public class ApiBusCompanyController {

    private final BusCompanyService busCompanyService;

    @GetMapping("/")
    public ResponseEntity<Object> list(@RequestParam Map<String, String> params) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return ResponseEntity.ok(busCompanyService.getAllAndFilter(params, BusCompanySpecification.class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusCompanyDTO> retrieve(@PathVariable Long id) {
        return ResponseEntity.ok(busCompanyService.getById(id));
    }
}