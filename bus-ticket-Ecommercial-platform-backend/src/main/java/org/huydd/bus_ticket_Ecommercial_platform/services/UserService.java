package org.huydd.bus_ticket_Ecommercial_platform.services;

import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service(value = "userDetailsService")
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
