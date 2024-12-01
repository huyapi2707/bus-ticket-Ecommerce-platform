package org.huydd.bus_ticket_Ecommercial_platform.controllers;

import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.requestModels.AuthenticationRequest;
import org.huydd.bus_ticket_Ecommercial_platform.requestModels.RegisterRequest;
import org.huydd.bus_ticket_Ecommercial_platform.responseModels.AuthenticationResponse;
import org.huydd.bus_ticket_Ecommercial_platform.services.AuthenticationService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

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
    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED)
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest payload) {

        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(authenticationService.registerUser(payload));
    }

    @GetMapping("/oauth2/{authorizationServer}")
    public ResponseEntity<String> handleOauth2(@PathVariable String authorizationServer, @RequestParam String state) {
        return ResponseEntity.ok(authenticationService.createLoginUrl(authorizationServer, state));
    }

    @GetMapping("/oauth2/{authorizationServer}/verify")
    public ResponseEntity<AuthenticationResponse> handleVerifyCode(@PathVariable String authorizationServer,
                                                                   @RequestParam String code
                                                                  ) throws GeneralSecurityException, IOException {
        return ResponseEntity.ok(authenticationService.verifyOauth2Request(authorizationServer, code));
    }

}
