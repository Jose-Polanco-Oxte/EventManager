package jpolanco.springbootapp.shared.infrastructure.auditory.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jpolanco.springbootapp.shared.infrastructure.auditory.AuditoryPersistence;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class AuditoryPersistenceImpl implements AuditoryPersistence {

    private final JpaAuditoryLogRepository jpaAuditoryLogRepository;

    private final ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(AuditoryPersistenceImpl.class);

    @Override
    public void save(Object event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            jpaAuditoryLogRepository.save(new AuditoryLog(event.getClass().getSimpleName(), json, Instant.now()));
        } catch (JsonProcessingException e) {
            logger.error("Error serializing event {}: {}", event.getClass().getSimpleName(), e.getMessage());
        }
    }
}
