package jpolanco.springbootapp.user.infrastructure.adapters.mappers.entity;

import jpolanco.springbootapp.user.domain.model.Token;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.TokenEntity;
import jpolanco.springbootapp.user.infrastructure.adapters.output.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class TokenEntityMapperImpl implements TokenEntityMapper {

    @Value("${jwt.expiration}")
    private long expiration;
    private final JpaUserRepository userRepository;

    @Override
    public TokenEntity toEntity(Token domain) {
        var userId = domain.getUserId();
        var userEntity = userRepository.findById(UUID.fromString(userId.getValue())).orElseThrow(() ->
                new IllegalArgumentException("User not found"));
        return new TokenEntity(
                domain.getId(),
                domain.getToken(),
                domain.getTokenType(),
                domain.getStatus(),
                userEntity
        );
    }

    @Override
    public Token toDomain(TokenEntity entity) {
        var maybeToken = Token.load(
                entity.getId(),
                entity.getUser().getId().toString(),
                entity.getToken(),
                entity.getType(),
                0,
                null,
                null,
                null,
                entity.getStatus()
        );
        if (maybeToken.isFailure()) {
            throw new IllegalArgumentException("Data do not match : " + maybeToken.getError());
        }
        return maybeToken.getValue();
    }
}
