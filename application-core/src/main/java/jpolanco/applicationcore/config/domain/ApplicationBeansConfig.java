package jpolanco.applicationcore.config.domain;

import jpolanco.applicationcore.user.application.components.FindPairs;
import jpolanco.applicationcore.user.domain.model.User;
import jpolanco.applicationcore.user.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class ApplicationBeansConfig {

    private final UserRepository userRepository;

    @Bean
    public FindPairs<User, UUID> findUserPairService() {
        return new FindPairs<>(userRepository);
    }
}
