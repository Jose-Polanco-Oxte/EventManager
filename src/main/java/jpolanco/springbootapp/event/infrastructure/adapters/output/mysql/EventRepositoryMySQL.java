package jpolanco.springbootapp.event.infrastructure.adapters.output.mysql;

import jpolanco.springbootapp.event.application.ports.output.EventRepository;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.event.infrastructure.adapters.mappers.entity.EventEntityMapper;
import jpolanco.springbootapp.event.infrastructure.adapters.output.repository.JpaEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class EventRepositoryMySQL implements EventRepository {

    private final JpaEventRepository jpaEventRepository;
    private final EventEntityMapper eventEntityMapper;

    @Override
    public void save(Event entity) {
        jpaEventRepository.save(eventEntityMapper.toEntity(entity));
    }

    @Override
    public Optional<Event> findById(String s) {
        return Optional.empty();
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void update(Event entity) {

    }

    @Override
    public boolean sameScheduleExists(Instant date, long duration) {
        return false;
    }

    @Override
    public boolean sameLocationExists(double latitude, double longitude) {
        return false;
    }
}
