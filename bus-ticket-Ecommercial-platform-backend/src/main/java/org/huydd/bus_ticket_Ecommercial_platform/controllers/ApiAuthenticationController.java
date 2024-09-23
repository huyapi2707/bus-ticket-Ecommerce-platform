package org.huydd.bus_ticket_Ecommercial_platform.controllers;

import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.requestObjects.AuthenticationRequest;
import org.huydd.bus_ticket_Ecommercial_platform.responseObjects.AuthenticationResponse;
import org.huydd.bus_ticket_Ecommercial_platform.requestObjects.LoginWithGoogleRequest;
import org.huydd.bus_ticket_Ecommercial_platform.requestObjects.RegisterRequest;
import org.huydd.bus_ticket_Ecommercial_platform.services.AuthenticationService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")

public class ApiAuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest payload) {
        return ResponseEntity.ok(authenticationService.authenticateUser(payload));
    }

    @PostMapping("/register")
    @Transactional(rollbackFor = RuntimeException.class)
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest payload) {

        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(authenticationService.registerUser(payload));
    }

    @PostMapping("/oauth2/google")
    @Transactional(rollbackFor = RuntimeException.class)
    public ResponseEntity<Object> loginWithGoogle(@RequestBody LoginWithGoogleRequest payload) {
        return ResponseEntity.ok(authenticationService.loginWithGoogle(payload));
    }
}
