package com.example.server.security;

import com.example.server.model.User;
import com.example.server.server.AuthenticationService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final AuthenticationService authenticationService;

    public JwtUserDetailsService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public JwtUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = authenticationService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return JwtUserDetails.fromUserToJwtUserDetails(user);
    }
}
