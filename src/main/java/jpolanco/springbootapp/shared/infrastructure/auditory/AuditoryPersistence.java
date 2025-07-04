package jpolanco.springbootapp.shared.infrastructure.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jpolanco.springbootapp.shared.infrastructure.auditory.AuditoryLog;
import jpolanco.springbootapp.shared.infrastructure.auditory.JpaAuditoryLogRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class AuditoryPersistence implements AuditoryPersistenceI {

    private final JpaAuditoryLogRepository jpaAuditoryLogRepository;

    private final ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(AuditoryPersistence.class);

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
