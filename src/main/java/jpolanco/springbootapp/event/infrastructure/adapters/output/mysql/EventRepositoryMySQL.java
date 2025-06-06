package jpolanco.springbootapp.event.infrastructure.adapters.output.mysql;

import jpolanco.springbootapp.event.application.ports.output.EventRepository;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.event.infrastructure.adapters.mappers.entity.EventEntityMapper;
import jpolanco.springbootapp.event.infrastructure.adapters.output.repository.JpaEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        return jpaEventRepository.findById(UUID.fromString(s)).map(eventEntityMapper::toDomain);
    }

    @Override
    public void deleteById(String s) {
        jpaEventRepository.deleteById(UUID.fromString(s));
    }

    @Override
    public void update(Event entity) {
        jpaEventRepository.save(eventEntityMapper.toEntity(entity));
    }

    @Override
    public boolean sameScheduleExists(Instant date, long duration) {
        return jpaEventRepository.existEventWithSameSchedule(date, duration);
    }

    @Override
    public boolean sameLocationExistsAndIsNotVirtual(double latitude, double longitude) {
        return jpaEventRepository.existEventWithSameLocationAndIsNotVirtual(latitude, longitude);
    }

    @Override
    public List<Event> findByCreatorIdAndSchedule(String creatorId, Instant date) {
        return jpaEventRepository.findByCreatorIdAndSchedule(UUID.fromString(creatorId), date)
                .stream()
                .map(eventEntityMapper::toDomain)
                .toList();
    }

    @Override
    public List<Event> findByCreatorId(String creatorId, Instant date) {
        return jpaEventRepository.findByCreator_Id(UUID.fromString(creatorId))
                .stream()
                .map(eventEntityMapper::toDomain)
                .toList();
    }
}
