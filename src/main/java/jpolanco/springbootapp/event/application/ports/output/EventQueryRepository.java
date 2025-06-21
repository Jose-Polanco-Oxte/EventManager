package jpolanco.springbootapp.event.application.ports.output;

import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.application.adapters.output.PageableRepository;
import jpolanco.springbootapp.shared.utils.CursorPageResult;
import jpolanco.springbootapp.shared.utils.PageResult;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface EventQueryRepository extends PageableRepository<Event, String> {
    Optional<Event> findById(String id);

    boolean sameScheduleExists(Instant date, long duration);

    boolean sameLocationExistsAndIsNotVirtual(double latitude, double longitude);

    List<Event> searchByName(String name, int size);

    List<Event> searchMyEventsByName(String name, String creatorId, int size);

    Optional<Event> findFirstConflictingEvent(Instant date, Instant endDate, String creatorId);

    PageResult<Event> findByCreatorId(String creatorId, int page, int size, String sortBy, String sortOrder);

    CursorPageResult<Event, String> findByCreatorId(String creatorId, String cursor, int size, String sortBy, String sortOrder);

    PageResult<Event> findPublicEvents(int page, int size, String sortBy, String sortOrder);

    CursorPageResult<Event, String> findPublicEvents(String cursor, int size, String sortBy, String sortOrder);
}
