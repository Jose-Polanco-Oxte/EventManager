package jpolanco.applicationcore.user.infrastructure.adapters.output.mongo;

import jpolanco.applicationcore.user.application.ports.output.TokenQueryRepository;
import jpolanco.applicationcore.user.infrastructure.adapters.output.persistence.TokenEntity;
import jpolanco.applicationcore.user.infrastructure.adapters.output.repositories.MongoTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TokenQueryMongo implements TokenQueryRepository {

    private final MongoTokenRepository mongoTokenRepository;

    @Override
    public Optional<String> findByToken(String token) {
        return mongoTokenRepository.findByToken(token)
                .map(TokenEntity::getToken);
    }

    @Override
    public List<String> findAllByUserId(UUID userId) {
        return mongoTokenRepository.findAllByUserUUID(userId.toString())
                .map(TokenEntity::getToken)
                .toList();
    }

    @Override
    public boolean existToken(String token) {
        return mongoTokenRepository.existsByToken(token);
    }

    @Override
    public int countByUserId(UUID userId) {
        return mongoTokenRepository.countByUserUUID(userId.toString());
    }

    @Override
    public List<TokenInfo> findAll() {
        return mongoTokenRepository.findAll()
                .stream()
                .map(t -> new TokenInfo(
                        UUID.fromString(t.getUserUUID()),
                        t.getToken(),
                        t.getStatus(),
                        t.getCreatedAt())
                )
                .toList();
    }

    @Override
    public boolean tokenIsValid(String token) {
        Optional<TokenEntity> maybeToken = mongoTokenRepository.findByToken(token);
        return maybeToken.isPresent() && maybeToken.get().getStatus().equals("ACTIVE");
    }
}
