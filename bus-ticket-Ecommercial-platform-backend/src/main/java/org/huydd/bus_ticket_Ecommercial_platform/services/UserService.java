package org.huydd.bus_ticket_Ecommercial_platform.services;

import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.UserDTO;
import org.huydd.bus_ticket_Ecommercial_platform.mappers.UserDTOMapper;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.User;
import org.huydd.bus_ticket_Ecommercial_platform.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service(value = "userDetailsService")
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final UserDTOMapper userDTOMapper;

    private boolean checkPermission(User user) {
        return true;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }


    public UserDTO toDTO(User user) {
        return userDTOMapper.apply(user);
    }

    public boolean isEmailExist(String email) {
        var user = userRepository.findByEmail(email);
        if (user.get() != null) {
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
}
