package org.huydd.bus_ticket_Ecommercial_platform.controllers;

import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.Site;
import org.huydd.bus_ticket_Ecommercial_platform.services.SiteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sites")
public class ApiSiteController {

    private final SiteService siteService;
    @GetMapping("/")
    public ResponseEntity<List<Site>> list() {
        return ResponseEntity.ok(siteService.getAll());
    }
}
