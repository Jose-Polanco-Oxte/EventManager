package jpolanco.applicationcore.config;

import jpolanco.applicationcore.config.auth.MyUserDetails;
import jpolanco.applicationcore.config.exceptions.AuthException;
import jpolanco.applicationcore.user.application.ports.output.UserQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final UserQueryRepository userRepository;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            final var user = userRepository.findByEmailAndNotDeleted(username)
                    .orElseThrow(() -> new AuthException("User not found"));
            List<String> roles = user.getRoles().get().stream().toList();
            return new MyUserDetails(
                    user.getUserId().getUUID(),
                    user.getEmail().getValue(),
                    user.getEncodedPassword().getValue(),
                    user.isActive(),
                    roles
            );
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var authProvider = new DaoAuthenticationProvider(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
