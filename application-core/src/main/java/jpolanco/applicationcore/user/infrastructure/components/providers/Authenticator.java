package jpolanco.applicationcore.user.infrastructure.components.providers;

import jpolanco.applicationcore.user.application.ports.input.AuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Authenticator implements AuthProvider {
    private final AuthenticationManager authenticationManager;

    @Override
    public void authenticate(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }
}
