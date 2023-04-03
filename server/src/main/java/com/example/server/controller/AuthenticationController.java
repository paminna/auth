package com.example.server.controller;

import com.example.server.request.AuthenticationRequest;
import com.example.server.request.AuthenticationResponse;
import com.example.server.server.AuthenticationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/auth")
    public AuthenticationResponse auth(@RequestBody AuthenticationRequest request) {
        return authenticationService.auth(request);
    }

    @PostMapping("/register")
    public void register(@RequestBody AuthenticationRequest request) {
        authenticationService.register(request);
    }
}
