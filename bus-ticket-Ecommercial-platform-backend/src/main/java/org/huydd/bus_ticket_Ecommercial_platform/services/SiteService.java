package org.huydd.bus_ticket_Ecommercial_platform.services;

import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.Site;
import org.huydd.bus_ticket_Ecommercial_platform.repositories.SiteRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SiteService {
    private final SiteRepository siteRepository;

    @Cacheable(value = "site", key = "new String(\"list\")")
    public List<Site> getAll() {
        return siteRepository.findAll();
    }

}
