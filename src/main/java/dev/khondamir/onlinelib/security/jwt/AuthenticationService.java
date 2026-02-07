package dev.khondamir.onlinelib.security.jwt;

import dev.khondamir.onlinelib.users.SignInRequest;
import dev.khondamir.onlinelib.users.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;

    private final JwtTokenManager jwtTokenManager;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtTokenManager jwtTokenManager) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenManager = jwtTokenManager;
    }

    public String authenticateUser(SignInRequest signInRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.login(),
                        signInRequest.password()
                )
        );
        return jwtTokenManager.generateToken(signInRequest.login());
    }

    public User getCurrentAuthenticatedUserOrThrow(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("Authentication not present");
        }
        return (User) authentication.getPrincipal();
    }

}
