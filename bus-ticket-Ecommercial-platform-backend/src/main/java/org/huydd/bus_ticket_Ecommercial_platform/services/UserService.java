package org.huydd.bus_ticket_Ecommercial_platform.services;

import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.UserDTO;
import org.huydd.bus_ticket_Ecommercial_platform.exceptions.AccessDeniedException;
import org.huydd.bus_ticket_Ecommercial_platform.exceptions.IdNotFoundException;
import org.huydd.bus_ticket_Ecommercial_platform.exceptions.NoContentException;
import org.huydd.bus_ticket_Ecommercial_platform.exceptions.NoPermissionException;
import org.huydd.bus_ticket_Ecommercial_platform.mappers.UserDTOMapper;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.BusCompany;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.TicketStatus;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.User;
import org.huydd.bus_ticket_Ecommercial_platform.repositories.UserRepository;
import org.huydd.bus_ticket_Ecommercial_platform.responseModels.PageableResponse;
import org.huydd.bus_ticket_Ecommercial_platform.specifications.TicketSpecification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Optional;

@Service(value = "userDetailsService")
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final UserDTOMapper userDTOMapper;

    private final CloudinaryService cloudinaryService;


    private final TicketService ticketService;

    private final TicketStatusService ticketStatusService;


    private boolean checkPermission(User user) {
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return authenticatedUser.getId() == user.getId();
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    public UserDTO toDTO(User user) {
        return userDTOMapper.apply(user);
    }

    public boolean isEmailExist(String email) {
        var user = userRepository.findByEmail(email);
        if (user.isPresent() && user.get() != null) {
            return true;
        }
        return false;
    }
    void saveUser(User user) {
        userRepository.save(user);
    }

    User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public UserDTO updateUser(Long id, UserDTO payload) throws IllegalAccessException, IOException {
        User user = userRepository.findById(id).get();
        if (!checkPermission(user)) throw new AccessDeniedException("Access denied");
        for (Field payloadField : payload.getClass().getDeclaredFields()) {
            payloadField.setAccessible(true);
            Object fieldData = payloadField.get(payload);
            String fieldName = payloadField.getName();
            // exclude fields
            if (fieldName.equals("username") && fieldName.equals("email") && fieldName.equals("password")) {
                continue;
            }
            if ( fieldData != null) {
                for (Field userField : user.getClass().getDeclaredFields()) {
                    if (userField.getName().equals(fieldName)) {
                        userField.setAccessible(true);
                        userField.set(user, fieldData);
                        break;
                    }
                }
            }

        }
        if (payload.getFile() != null) {
            String url = cloudinaryService.uploadFile(payload.getFile());
            user.setAvatar(url);
        }
        userRepository.save(user);
        return userDTOMapper.apply(user);
    }

    public UserDTO getSelfInfo() {
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return toDTO(user);
    }

    public BusCompany getManagedCompany(Long userId){
        Optional<User> optionalUser =  userRepository.findById(userId);
        if (optionalUser.isEmpty()) throw new IdNotFoundException("User id is not found");
        User user = optionalUser.get();
        if (!checkPermission(user)) throw new NoPermissionException("Dont don't have permission to access this content");
        BusCompany company = user.getManaged();
        if (company != null && company.getIsActive() && company.getIsVerified()) {
            return company;
        }
        else throw new NoContentException("No bus company found");
    }

    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username).orElse(null);
    }

    public PageableResponse getTickets(Long userId, Map<String, Object> params) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        User user = userRepository.findById(userId).get();
        if (!checkPermission((user))) throw  new NoPermissionException("You don't have permission to access this content");
        params.put("customer", user);
        if (params.containsKey("status")) {
            TicketStatus status = ticketStatusService.getStatusByName(params.get("status").toString());
            params.put("status", status);

        }
        return (PageableResponse) ticketService.getAllAndFilter(params, TicketSpecification.class, 10);
    }


}
