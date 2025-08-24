package jpolanco.applicationcore.user.infrastructure.adapters.output.mongo;

import jpolanco.applicationcore.user.application.ports.output.TokenCommandRepository;
import jpolanco.applicationcore.user.infrastructure.adapters.output.persistence.TokenEntity;
import jpolanco.applicationcore.user.infrastructure.adapters.output.repositories.MongoTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TokenCommandMongo implements TokenCommandRepository {

    private final MongoTokenRepository mongoTokenRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public void save(String token, UUID userId) {
        TokenEntity tokenEntity = TokenEntity.of(token).withUserUUID(userId.toString()).withTypeBearer();
        mongoTokenRepository.save(tokenEntity);
    }

    @Override
    public void deleteByToken(String token) {
        mongoTokenRepository.deleteByToken(token);
    }

    @Override
    public void revokeByToken(String token) {
        Query query = new Query();
        query.addCriteria(Criteria.where("token").is(token).and("status").is("ACTIVE"));

        Update update = new Update();
        update.set("status", "REVOKED");
        mongoTemplate.updateFirst(query, update, TokenEntity.class);
    }

    @Override
    public void deleteAllByUserId(UUID userId) {
        mongoTokenRepository.deleteAllByUserUUID(userId.toString());
    }

    @Override
    public void deleteAllByStatus(String status) {
        mongoTokenRepository.deleteAllByStatus(status);
    }

    @Override
    public void expireToken(String token) {
        Query query = new Query();
        query.addCriteria(Criteria.where("token").is(token).and("status").is("ACTIVE"));

        Update update = new Update();
        update.set("status", "EXPIRED");
        mongoTemplate.updateFirst(query, update, TokenEntity.class);
    }
}
