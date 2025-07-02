package jpolanco.springbootapp.user.infrastructure.adapters.mappers.entity;

import jpolanco.springbootapp.shared.infrastructure.mappers.TestableMapper;
import jpolanco.springbootapp.user.application.utils.TokenE;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.TokenEntity;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class TokenEntityMapperImpl implements TokenEntityMapper, TestableMapper<TokenEntity, TokenE> {

    @Override
    public TokenEntity toEntity(TokenE domain) {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setToken(domain.getToken());
        tokenEntity.setStatus(domain.getStatus());
        var userEntity = new UserEntity();
        userEntity.setId(domain.getUserId());
        tokenEntity.setUser(userEntity);
        return tokenEntity;
    }

    @Override
    public TokenE toDomain(TokenEntity entity) {
        return new TokenE(
                entity.getToken(),
                entity.getId(),
                entity.getStatus(),
                Instant.EPOCH
        );
    }

    @Override
    public TokenEntity fromDomain(TokenE token, UserEntity user) {
        var tokenEntity = new TokenEntity();
        tokenEntity.setToken(token.getToken());
        tokenEntity.setStatus(token.getStatus());
        tokenEntity.setUser(user);
        return tokenEntity;
    }

    @Override
    public TokenE fromPersistence(TokenEntity entity) {
        return new TokenE(
                entity.getToken(),
                entity.getId(),
                entity.getStatus(),
                Instant.EPOCH
        );
    }
}
