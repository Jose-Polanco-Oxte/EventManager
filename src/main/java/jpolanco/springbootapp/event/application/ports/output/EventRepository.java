package jpolanco.springbootapp.event.application.ports.output;

import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.utils.CursorPageResult;
import jpolanco.springbootapp.shared.utils.PageResult;
import jpolanco.springbootapp.shared.application.adapters.output.CRUDRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends CRUDRepository<Event, String> {

    boolean sameScheduleExists(Instant date, long duration);

    boolean sameLocationExistsAndIsNotVirtual(double latitude, double longitude);

    List<Event> searchByName(String name, int size);

    List<Event> searchPublicByName(String name, int size);

    List<Event> searchMyEventsByName(String name, String creatorId, int size);

    List<Event> findByCreatorIdAndSchedule(String creatorId, Instant date);

    List<Event> findByCreatorId(String creatorId);

    Optional<Event> findFirstConflictingEvent(Instant date, Instant endDate, String creatorId);

    PageResult<Event> findByCreatorId(String creatorId, int page, int size, String sortBy, String sortOrder);

    CursorPageResult<Event, String> findByCreatorId(String creatorId, String cursor, int size, String sortBy, String sortOrder);

    List<Event> findAll();

    PageResult<Event> findAll(int page, int size, String sortBy, String sortOrder);

    CursorPageResult<Event, String> findAll(String cursor, int size, String sortBy, String sortOrder);

    PageResult<Event> findPublicEvents(int page, int size, String sortBy, String sortOrder);

    CursorPageResult<Event, String> findPublicEvents(String cursor, int size, String sortBy, String sortOrder);

    void deleteByIdAndCreatorId(String id, String creatorId);
}