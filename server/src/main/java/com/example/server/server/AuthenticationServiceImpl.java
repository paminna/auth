package com.example.server.server;

import com.example.server.exception.AuthenticationException;
import com.example.server.model.User;
import com.example.server.request.AuthenticationRequest;
import com.example.server.request.AuthenticationResponse;
import com.example.server.repository.UserRepository;
import com.example.server.security.JwtProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final String USER_NOT_FOUND = "User not found";
    private static final String INVALID_PASSWORD = "Invalid password";
    private static final String EMPTY_PASSWORD = "Password cannot be empty";
    private static final String USER_ALREADY_REGISTERED = "Username is already registered";


    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthenticationServiceImpl(UserRepository userRepository, JwtProvider jwtProvider, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public AuthenticationResponse auth(AuthenticationRequest request) {
        User user = findByUsername(request.getUsername())
                .orElseThrow(() -> new AuthenticationException(USER_NOT_FOUND));
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new AuthenticationResponse(jwtProvider.generateToken(request.getUsername()));
        } else {
            throw new AuthenticationException(INVALID_PASSWORD);
        }
    }

    @Transactional
    @Override
    public void register(AuthenticationRequest request) {
        if (request.getPassword() == null) throw new AuthenticationException(EMPTY_PASSWORD);
        User newUser = new User(request.getUsername(), request.getPassword());
        if (findByUsername(newUser.getUsername()).isEmpty()) {
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
//            newUser.setEncodedPass(request.getPassword());
            userRepository.save(newUser);
        } else {
            throw new AuthenticationException(USER_ALREADY_REGISTERED);
        }
    }

}
