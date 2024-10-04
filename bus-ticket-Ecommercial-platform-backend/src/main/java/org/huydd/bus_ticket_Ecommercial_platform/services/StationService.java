package org.huydd.bus_ticket_Ecommercial_platform.services;

import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.Station;
import org.huydd.bus_ticket_Ecommercial_platform.repositories.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StationService {
    private final StationRepository stationRepository;
    public List<Station> getBySiteName(String siteName) {
        return stationRepository.findAllBySiteName(siteName);
    }
}
