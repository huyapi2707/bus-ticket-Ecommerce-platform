package org.huydd.bus_ticket_Ecommercial_platform.controllers;

import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.AuthenticationRequest;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.AuthenticationResponse;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.LoginWithGoogleRequest;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.RegisterRequest;
import org.huydd.bus_ticket_Ecommercial_platform.services.AuthenticationService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

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
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest payload) {
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(authenticationService.registerUser(payload));
    }

    @PostMapping("/oauth2/google")
    public ResponseEntity<Object> loginWithGoogle(@RequestBody LoginWithGoogleRequest payload) {
        return ResponseEntity.ok(authenticationService.loginWithGoogle(payload));
    }
}
