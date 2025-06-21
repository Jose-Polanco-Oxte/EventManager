package jpolanco.springbootapp.user.infrastructure.adapters.mappers.entity;

import jpolanco.springbootapp.user.application.utils.TokenE;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.TokenEntity;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.UserEntity;
import jpolanco.springbootapp.user.infrastructure.adapters.output.repository.JpaUserRepository;
import jpolanco.springbootapp.user.infrastructure.errors.UserIntegrity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TokenEntityMapperImpl implements TokenEntityMapper {
    private final JpaUserRepository userRepository;

    @Override
    public TokenEntity toEntity(TokenE domain) {
        var userId = domain.getUserId();
        var userEntity = userRepository.findById(UUID.fromString(userId)).orElseThrow(() ->
                new UserIntegrity("User not found", 402));
        var tokenEntity = new TokenEntity();
        tokenEntity.setToken(domain.getToken());
        tokenEntity.setStatus(domain.getStatus());
        tokenEntity.setUser(userEntity);
        return tokenEntity;
    }

    @Override
    public TokenEntity toEntity(TokenE token, UserEntity user) {
        var tokenEntity = new TokenEntity();
        tokenEntity.setToken(token.getToken());
        tokenEntity.setStatus(token.getStatus());
        tokenEntity.setUser(user);
        return tokenEntity;
    }

    @Override
    public TokenE toDomain(TokenEntity entity) {
        return new TokenE(
                entity.getToken(),
                entity.getUser().getId().toString(),
                entity.getStatus(),
                Instant.EPOCH
        );
    }
}
