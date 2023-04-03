package com.example.server.server;

import com.example.server.model.User;
import com.example.server.request.AuthenticationRequest;
import com.example.server.request.AuthenticationResponse;

import java.util.Optional;

public interface AuthenticationService {

    Optional<User> findByUsername(String username);

    AuthenticationResponse auth(AuthenticationRequest authenticationRequest);

    void register(AuthenticationRequest authenticationRequest);
}
