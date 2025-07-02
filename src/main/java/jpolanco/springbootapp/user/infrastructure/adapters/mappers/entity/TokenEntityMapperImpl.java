package jpolanco.springbootapp.user.infrastructure.adapters.mappers.entity;

import jpolanco.springbootapp.user.application.utils.TokenE;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.TokenEntity;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.UserEntity;
import jpolanco.springbootapp.user.infrastructure.adapters.output.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class TokenEntityMapperImpl implements TokenEntityMapper {
    private final JpaUserRepository jpaUserRepository;

    @Override
    public TokenEntity toEntity(TokenE domain) {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setToken(domain.getToken());
        tokenEntity.setStatus(domain.getStatus());
        tokenEntity.setUser(jpaUserRepository.getReferenceById(domain.getUserId()));
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
    public TokenE load(TokenEntity entity) {
        return new TokenE(
                entity.getToken(),
                entity.getId(),
                entity.getStatus(),
                Instant.EPOCH
        );
    }
}
