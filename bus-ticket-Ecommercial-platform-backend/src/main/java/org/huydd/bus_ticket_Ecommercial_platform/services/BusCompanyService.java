package org.huydd.bus_ticket_Ecommercial_platform.services;

import org.huydd.bus_ticket_Ecommercial_platform.dtos.BusCompanyDTO;
import org.huydd.bus_ticket_Ecommercial_platform.mappers.BusCompanyDTOMapper;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.BusCompany;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.Route;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.User;
import org.huydd.bus_ticket_Ecommercial_platform.repositories.BusCompanyRepository;
import org.huydd.bus_ticket_Ecommercial_platform.specifications.BusCompanySpecification;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BusCompanyService extends AbstractPaginateAndFilterService {

    private final RouteService routeService;
    private final BusCompanyRepository busCompanyRepository;

    private final BusCompanyDTOMapper busCompanyDTOMapper;

    private final CloudinaryService cloudinaryService;

    private final MailSenderService mailSenderService;

    private final ConfigurationService configurationService;

    private final UserService userService;

    public BusCompanyService(BusCompanyRepository busCompanyRepository,
                             BusCompanyDTOMapper busCompanyDTOMapper,
                             RouteService routeService,
                             CloudinaryService cloudinaryService,
                             MailSenderService mailSenderService,
                             ConfigurationService configurationService,
                             UserService userService) {
        super(busCompanyRepository, busCompanyDTOMapper);
        this.routeService = routeService;
        this.busCompanyRepository = busCompanyRepository;
        this.busCompanyDTOMapper = busCompanyDTOMapper;
        this.cloudinaryService = cloudinaryService;
        this.mailSenderService = mailSenderService;
        this.configurationService = configurationService;
        this.userService = userService;
    }

    @Cacheable(value = "companies", keyGenerator = "redisKeyGenerator")
    public Object handleGetAllAndFilter(Map<String, Object> params,

                                        int pageSize) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        return super.getAllAndFilter(params, BusCompanySpecification.class, pageSize);
    }

    @Cacheable(value = "companies", key = "#id")
    public BusCompany getById(Long id) {
       return (BusCompany) super.getById(id);
    }


    public List<Route> getRoutes(Long id) {
        return routeService.getByBusCompanyId(id);
    }

    public BusCompany createCompany(BusCompanyDTO payload) throws IOException {
        String name = payload.getName();
        String phone = payload.getPhone();
        String email = payload.getEmail();
        Long mangerId = payload.getManagerId();
        MultipartFile avatarFile = payload.getAvatarFile();

        Optional<BusCompany> dbBusCompany = busCompanyRepository.findByManagerId(mangerId);
        if (dbBusCompany.isPresent()) throw new IllegalArgumentException("Company with this manger is existed");

        Optional<User> manager = userService.getById(mangerId);
        if (manager.isEmpty()) throw new IllegalArgumentException("Manager id is invalid");


        if (!cloudinaryService.validateImageFile(avatarFile))
            throw new IllegalArgumentException("Invalid mimetypes for avatar");
        String avatarUrl = cloudinaryService.uploadFile(avatarFile);

        Double defaultOperationCost = configurationService.getDefaultOperationCost();
        BusCompany busCompany = BusCompany.builder()
                .email(email)
                .name(name)
                .phone(phone)
                .isVerified(false)
                .operationCost(defaultOperationCost)
                .manager(manager.get())
                .avatar(avatarUrl)
                .build();
        busCompanyRepository.save(busCompany);
        return busCompany;
    }
}
