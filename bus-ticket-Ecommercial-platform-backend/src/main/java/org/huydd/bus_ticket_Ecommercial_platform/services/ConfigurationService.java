package org.huydd.bus_ticket_Ecommercial_platform.services;


import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.Configuration;
import org.huydd.bus_ticket_Ecommercial_platform.repositories.ConfigurationRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfigurationService {
    private final ConfigurationRepository configurationRepository;

    public Integer getJwtExpiredDuration() {
        Configuration configuration = configurationRepository.findFirstByOrderByIdAsc();
        return  configuration.getJwtExpirationDuration();
    }

    public Integer getMaxCarryOnCargoKg() {
        Configuration configuration = configurationRepository.findFirstByOrderByIdAsc();
        return configuration.getMaxCarryOnLuggageKg();
    }
}
