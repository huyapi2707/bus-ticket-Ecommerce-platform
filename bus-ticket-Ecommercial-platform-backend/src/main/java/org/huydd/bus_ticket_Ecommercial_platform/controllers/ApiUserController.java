package org.huydd.bus_ticket_Ecommercial_platform.controllers;

import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.TicketDTO;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.UserDTO;
import org.huydd.bus_ticket_Ecommercial_platform.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class ApiUserController {

    private final UserService userService;

    @GetMapping(path = "/self")
    public ResponseEntity<UserDTO> self() {
        return ResponseEntity.ok(userService.getSelfInfo());
    }

    @PatchMapping(path = "/{id}")
    @Transactional(rollbackFor = RuntimeException.class)
    public ResponseEntity<UserDTO> partialUpdate(@PathVariable Long id,
                                                 @ModelAttribute UserDTO payload
    ) throws IOException, IllegalAccessException {
        return ResponseEntity.ok(userService.updateUser(id, payload));
    }

    @GetMapping(path = "/{id}/tickets")
    public ResponseEntity<List<TicketDTO>> getTickets(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getTickets(id));
    }
}
