package jpolanco.applicationcore.audit.mongo;

import jpolanco.applicationcore.audit.persistence.base.BaseLog;
import jpolanco.applicationcore.audit.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LogRepositoryMongo implements LogRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public <T extends BaseLog> void save(T log) {
        System.out.println("Saving log: " + log);
        if (log == null) {
            throw new IllegalArgumentException("Log cannot be null");
        }
        mongoTemplate.save(log);
    }
}
