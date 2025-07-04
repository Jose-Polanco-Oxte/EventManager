package jpolanco.springbootapp.user.infrastructure.adapters.mappers.entity;

import jpolanco.springbootapp.user.application.utils.TokenE;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.TokenEntity;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class TokenEntityMapperImpl implements TokenEntityMapper {
    private final UserEntityMapper userEntityMapper;

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
                userEntityMapper.fromPersistence(entity.getUser()),
                entity.getStatus(),
                Instant.EPOCH
        );
    }
}
