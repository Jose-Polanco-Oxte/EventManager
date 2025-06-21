package jpolanco.springbootapp.event.infrastructure.adapters.output.mysql;

import jpolanco.springbootapp.event.application.ports.output.EventCommandRepository;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.event.infrastructure.adapters.mappers.entity.EventEntityMapper;
import jpolanco.springbootapp.event.infrastructure.adapters.output.repository.JpaEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class EventCommandMySQL implements EventCommandRepository {
    private final JpaEventRepository jpaEventRepository;
    private final EventEntityMapper eventEntityMapper;

    @Override
    public Event save(Event entity) {
        var event = jpaEventRepository.save(eventEntityMapper.toEntity(entity));
        return eventEntityMapper.toDomain(event);
    }

    @Override
    public void deleteById(String s) {
        jpaEventRepository.deleteById(UUID.fromString(s));
    }
}
