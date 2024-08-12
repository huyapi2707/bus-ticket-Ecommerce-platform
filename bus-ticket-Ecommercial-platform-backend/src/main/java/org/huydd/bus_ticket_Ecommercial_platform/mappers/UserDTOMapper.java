package org.huydd.bus_ticket_Ecommercial_platform.mappers;

import org.huydd.bus_ticket_Ecommercial_platform.dtos.UserDTO;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserDTOMapper implements Function<User, UserDTO> {
    @Override
    public UserDTO apply(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstname(user.getFirstName())
                .lastname(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .avatar(user.getAvatar())
                .role(user.getRole().getName())
                .build();
    }
}
