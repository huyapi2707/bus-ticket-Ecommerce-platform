package org.huydd.bus_ticket_Ecommercial_platform.services;



import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.BusCompanyDTO;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.PageableResponse;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.SearchCriteria;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.BusCompany;
import org.huydd.bus_ticket_Ecommercial_platform.repositories.FilterAndPaginateRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public abstract class AbstractPaginateAndFilterService {
    protected final FilterAndPaginateRepository repository;
    protected final Function dtoMapper;

    public Object toDTO(Object obj) {
        return  dtoMapper.apply(obj);
    }

    public Object getAllAndFilter(Map<String, String> params, Class<? extends Specification>  specificationClass) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        List<Specification> specifications = new ArrayList<>();
        boolean isActiveKeyExist = params.containsKey("isActive");

        Boolean isActive = true;
        if (isActiveKeyExist) {
            isActive = Boolean.valueOf(params.get("isActive"));
            params.remove("isActive");
        }

        boolean isPageKeyExist = params.containsKey("page");
        Pageable pageRequest = null;
        if (isPageKeyExist && params.get("page") != null) {
            try {
                int page = Integer.parseInt(params.get("page"));
                if ( page > 0) {
                    pageRequest = PageRequest.of(page - 1, 16);
                    params.remove("page");
                }

            }catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        specifications.add(specificationClass.getDeclaredConstructor(SearchCriteria.class)
                .newInstance(new SearchCriteria("isActive", ":", isActive)));

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = new String (entry.getValue());
            specifications.add(specificationClass.getDeclaredConstructor(SearchCriteria.class)
                    .newInstance(new SearchCriteria(key, ":", value)));

        }
        if (pageRequest != null) {
            Page<Object> pageableResult = repository.findAll(Specification.allOf(specifications.toArray(Specification[]::new)),  pageRequest);
            return PageableResponse.builder()
                    .totalResults(pageableResult.getNumberOfElements())
                    .currentPage(pageableResult.getNumber() + 1)
                    .totalPage(pageableResult.getTotalPages())
                    .results(pageableResult
                            .getContent()
                            .stream()
                            .map(dtoMapper)
                            .collect(Collectors.toList()))
                    .build();
        }
        else {
            return repository.findAll(Specification.allOf(specifications
                    .toArray(Specification[]::new)))
                    .stream().map(dtoMapper::apply).collect(Collectors.toList());
        }
    }
}
