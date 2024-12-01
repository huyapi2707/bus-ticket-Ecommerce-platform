package org.huydd.bus_ticket_Ecommercial_platform.services;

import org.huydd.bus_ticket_Ecommercial_platform.dao.RouteDAO;
import org.huydd.bus_ticket_Ecommercial_platform.mappers.RouteDTOMapper;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.Route;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.Trip;
import org.huydd.bus_ticket_Ecommercial_platform.repositories.RouteRepository;
import org.huydd.bus_ticket_Ecommercial_platform.responseModels.PageableResponse;
import org.huydd.bus_ticket_Ecommercial_platform.specifications.RouteSpecification;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RouteService extends AbstractPaginateAndFilterService {

    private TripService tripService;

    private RouteRepository routeRepository;

    private final RouteDAO routeDAO;

    private RouteDTOMapper routeDTOMapper;

    public RouteService(RouteRepository routeRepository,
                        RouteDTOMapper routeDTOMapper,
                        TripService tripService, RouteDAO routeDAO) {
        super(routeRepository, routeDTOMapper);
        this.routeRepository = routeRepository;
        this.tripService = tripService;
        this.routeDTOMapper = routeDTOMapper;
        this.routeDAO = routeDAO;
    }

    @Cacheable(value = "route",key = "#id")
    public Route getById(Long id) {
        return (Route) super.getById(id);
    }

    @Cacheable(value = "routes", keyGenerator = "redisKeyGenerator", condition = "#params.size() < 2")
    public Object handleGetAllAndFilter(Map<String, Object> params,
                                        int pageSize) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        return super.getAllAndFilter(params, RouteSpecification.class, pageSize);
    }


    public List<Trip> getTrips(Long id) {
        return tripService.getByRouteId(id);
    }

    public List<Route> getByBusCompanyId(Long busCompanyId) {
        return routeRepository.findAllByCompanyIdAndIsActive(busCompanyId, true);

    }

    public Object handleSearch(Map<String, String> params) {
        int fromSite = -1;
        int toSite = -1;
        Timestamp fromDate = null;
        Timestamp toDate = null;
        int page = -1;
        if (params.containsKey("page")) {
            page = Integer.parseInt(params.get("page"));
        }
        if (params.containsKey("fromSite")) {
            fromSite = Integer.parseInt(params.get("fromSite"));
        }
        if (params.containsKey("toSite")) {
            toSite = Integer.parseInt(params.get("toSite"));
        }
        if (params.containsKey("startDate")) {
            fromDate = new Timestamp(Long.parseLong(params.get("startDate")));
            toDate = new Timestamp(fromDate.getTime() + (24 * 60 * 60 * 1000));
        }
        int totalResult = Integer.parseInt(routeDAO.countByFromSiteAndToSiteAndDepartDate(fromSite,
                toSite,
                fromDate,
                toDate).toString());
        List<Route> result = routeDAO.findAllByFromSiteAndToSiteAndDepartDate(fromSite,
                toSite,
                fromDate,
                toDate,
                page);
        if (page > 0) {
            return PageableResponse.builder()
                    .totalResults(totalResult)
                    .totalPage((int) Math.ceil((totalResult / 15)))
                    .currentPage(page)
                    .results(result.stream().map(routeDTOMapper::apply).collect(Collectors.toList()))
                    .build();
        }
        return result.stream().map(routeDTOMapper::apply).collect(Collectors.toList());
    }

}
