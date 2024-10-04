package org.huydd.bus_ticket_Ecommercial_platform.controllers;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.BusCompanyDTO;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.TicketDTO;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.UserDTO;
import org.huydd.bus_ticket_Ecommercial_platform.responseObjects.PageableResponse;
import org.huydd.bus_ticket_Ecommercial_platform.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<PageableResponse> getTickets(@PathVariable Long id, @RequestParam Map<String, Object> params) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        return ResponseEntity.ok(userService.getTickets(id, params));
    }

    @GetMapping(path = "/{id}/managed_company")
    public ResponseEntity<BusCompanyDTO> getManagedCompany(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getManagedCompany(id));
    }
}
