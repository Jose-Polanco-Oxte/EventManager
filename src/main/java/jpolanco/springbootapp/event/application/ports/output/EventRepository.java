package jpolanco.springbootapp.event.application.ports.output;

import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.domain.CRUDRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends CRUDRepository<Event, String> {

    boolean sameScheduleExists(Instant date, long duration);

    boolean sameLocationExistsAndIsNotVirtual(double latitude, double longitude);

    List<Event> findByCreatorIdAndSchedule(String creatorId, Instant date);

    List<Event> findByCreatorId(String creatorId);

    List<Event> findAll();

    void deleteByIdAndCreatorId(String id, String creatorId);
}