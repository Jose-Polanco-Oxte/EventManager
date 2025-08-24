package jpolanco.applicationcore.user.infrastructure.adapters.output.repositories;

import jpolanco.applicationcore.user.infrastructure.adapters.output.persistence.TokenEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.util.Streamable;

import java.util.Optional;

public interface MongoTokenRepository extends MongoRepository<TokenEntity, String> {

    void deleteAllByUserUUID(String userUUID);

    void deleteAllByStatus(String status);

    void deleteByToken(String token);

    Optional<TokenEntity> findByToken(String token);

    Streamable<TokenEntity> findAllByUserUUID(String userUUID);

    boolean existsByToken(String token);

    int countByUserUUID(String userUUID);
}