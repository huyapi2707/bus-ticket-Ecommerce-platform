package org.huydd.bus_ticket_Ecommercial_platform.services;

import org.huydd.bus_ticket_Ecommercial_platform.dtos.BusCompanyDTO;
import org.huydd.bus_ticket_Ecommercial_platform.exceptions.IdNotFoundException;
import org.huydd.bus_ticket_Ecommercial_platform.mappers.BusCompanyDTOMapper;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.BusCompany;
import org.huydd.bus_ticket_Ecommercial_platform.repositories.BusCompanyRepository;

import org.springframework.stereotype.Service;

@Service
public class BusCompanyService extends AbstractPaginateAndFilterService {


    public BusCompanyService(BusCompanyRepository repository,
                             BusCompanyDTOMapper dtoMapper) {
        super(repository, dtoMapper);

    }


    public BusCompanyDTO getById(Long id) {
        var result = repository.findById(id);
        if (result.isPresent()) {
            return (BusCompanyDTO) dtoMapper.apply(result.get());
        }
        else throw new IdNotFoundException("Id not found");
    }
}
