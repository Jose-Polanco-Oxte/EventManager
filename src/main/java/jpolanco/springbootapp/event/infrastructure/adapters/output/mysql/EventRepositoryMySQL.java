package jpolanco.springbootapp.event.infrastructure.adapters.output.mysql;

import jpolanco.springbootapp.event.application.ports.output.EventRepository;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.event.infrastructure.adapters.mappers.entity.EventEntityMapper;
import jpolanco.springbootapp.event.infrastructure.adapters.output.persistence.EventEntity;
import jpolanco.springbootapp.event.infrastructure.adapters.output.repository.JpaEventRepository;
import jpolanco.springbootapp.shared.application.CursorPageResult;
import jpolanco.springbootapp.shared.application.PageResult;
import jpolanco.springbootapp.shared.infrastructure.components.PageAux;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
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
    public Event save(Event entity) {
        var event = jpaEventRepository.save(eventEntityMapper.toEntity(entity));
        return eventEntityMapper.toDomain(event);
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
    public Event update(Event entity) {
        var event = jpaEventRepository.save(eventEntityMapper.toEntity(entity));
        return eventEntityMapper.toDomain(event);
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
    public List<Event> searchByName(String name, int size) {
        return jpaEventRepository.searchByName(name, PageRequest.of(0, size))
                .stream()
                .map(eventEntityMapper::toDomain)
                .toList();
    }

    @Override
    public List<Event> searchPublicByName(String name, int size) {
        return jpaEventRepository.searchPublicByName(name, PageRequest.of(0, size))
                .stream()
                .map(eventEntityMapper::toDomain)
                .toList();
    }

    @Override
    public List<Event> searchMyEventsByName(String name, String creatorId, int size) {
        return jpaEventRepository.searchMyEventsByName(name, UUID.fromString(creatorId), PageRequest.of(0, size))
                .stream()
                .map(eventEntityMapper::toDomain)
                .toList();
    }

    @Override
    public List<Event> findByCreatorIdAndSchedule(String creatorId, Instant date) {
        return jpaEventRepository.findByCreatorIdAndSchedule(UUID.fromString(creatorId), date)
                .stream()
                .map(eventEntityMapper::toDomain)
                .toList();
    }

    @Override
    public List<Event> findByCreatorId(String creatorId) {
        return jpaEventRepository.findByCreatorId(UUID.fromString(creatorId))
                .stream()
                .map(eventEntityMapper::toDomain)
                .toList();
    }

    @Override
    public PageResult<Event> findByCreatorId(String creatorId, int page, int size, String sortBy, String sortOrder) {
        Slice<EventEntity> pageRequest;
        if (sortBy.equalsIgnoreCase("none")) {
            pageRequest = jpaEventRepository.findByCreator_Id(UUID.fromString(creatorId), PageRequest.of(page, size));
        } else {
            var sort = sortOrder.equalsIgnoreCase("asc") ?
                    Sort.by(sortBy).ascending() :
                    Sort.by(sortBy).descending();
            pageRequest = jpaEventRepository.findByCreator_Id(UUID.fromString(creatorId), PageRequest.of(page, size, sort));
        }
        return new PageResult<>(
                pageRequest.get().map(eventEntityMapper::toDomain).toList(),
                pageRequest.getNumber(),
                pageRequest.getSize(),
                pageRequest.hasNext()
        );
    }

    @Override
    public CursorPageResult<Event, String> findByCreatorId(String creatorId, String cursor, int size, String sortBy, String sortOrder) {
        UUID creatorUUID = UUID.fromString(creatorId);
        var sort = PageAux.resolveSort(sortBy, sortOrder);
        var pageRequest = PageRequest.of(0, size, sort);

        Slice<EventEntity> slice;
        if (cursor.equalsIgnoreCase("none")) {
            slice = jpaEventRepository.findByCreator_Id(UUID.fromString(creatorId), pageRequest);
        } else {
            var cursorData = PageAux.decodeCursor(cursor);
            slice = jpaEventRepository.findByCreator_Id(
                    cursorData.date(),
                    cursorData.id(),
                    creatorUUID,
                    pageRequest
            );
        }
        return getEventStringCursorPageResult(slice);
    }

    @Override
    public List<Event> findAll() {
        return jpaEventRepository.findAll()
                .stream()
                .map(eventEntityMapper::toDomain)
                .toList();
    }

    @Override
    public PageResult<Event> findAll(int page, int size, String sortBy, String sortOrder) {
        Slice<EventEntity> pageRequest;
        if (sortBy.equalsIgnoreCase("none")) {
            pageRequest = jpaEventRepository.findAll(PageRequest.of(page, size));
        } else {
            var sort = sortOrder.equalsIgnoreCase("asc") ?
                    Sort.by(sortBy).ascending() :
                    Sort.by(sortBy).descending();
            pageRequest = jpaEventRepository.findAll(PageRequest.of(page, size, sort));
        }
        return new PageResult<>(
                pageRequest.get().map(eventEntityMapper::toDomain).toList(),
                pageRequest.getNumber(),
                pageRequest.getSize(),
                pageRequest.hasNext()
        );
    }

    @Override
    public CursorPageResult<Event, String> findAll(String cursor, int size, String sortBy, String sortOrder) {
        var sort = PageAux.resolveSort(sortBy, sortOrder);
        var pageRequest = PageRequest.of(0, size, sort);

        Slice<EventEntity> slice;
        if (cursor.equalsIgnoreCase("none")) {
            slice = jpaEventRepository.findAll(pageRequest);
        } else {
            var cursorData = PageAux.decodeCursor(cursor);
            slice = jpaEventRepository.findAll(
                    cursorData.date(),
                    cursorData.id(),
                    pageRequest
            );
        }
        return getEventStringCursorPageResult(slice);
    }

    @Override
    public PageResult<Event> findPublicEvents(int page, int size, String sortBy, String sortOrder) {
        Slice<EventEntity> pageRequest;
        if (sortBy.equalsIgnoreCase("none")) {
            pageRequest = jpaEventRepository.findByPreferencesPublic(PageRequest.of(page, size));
        } else {
            var sort = sortOrder.equalsIgnoreCase("asc") ?
                    Sort.by(sortBy).ascending() :
                    Sort.by(sortBy).descending();
            pageRequest = jpaEventRepository.findByPreferencesPublic(PageRequest.of(page, size, sort));
        }
        return new PageResult<>(
                pageRequest.get().map(eventEntityMapper::toDomain).toList(),
                pageRequest.getNumber(),
                pageRequest.getSize(),
                pageRequest.hasNext()
        );
    }

    @Override
    public CursorPageResult<Event, String> findPublicEvents(String cursor, int size, String sortBy, String sortOrder) {
        var sort = PageAux.resolveSort(sortBy, sortOrder);
        var pageRequest = PageRequest.of(0, size, sort);

        Slice<EventEntity> slice;
        if (cursor.equalsIgnoreCase("none")) {
            slice = jpaEventRepository.findByPreferencesPublic(pageRequest);
        } else {
            var cursorData = PageAux.decodeCursor(cursor);
            slice = jpaEventRepository.findByPreferencesPublic(
                    cursorData.date(),
                    cursorData.id(),
                    pageRequest
            );
        }
        return getEventStringCursorPageResult(slice);
    }

    private CursorPageResult<Event, String> getEventStringCursorPageResult(Slice<EventEntity> slice) {
        if (slice.isEmpty()) {
            return new CursorPageResult<>(List.of(), null, false);
        }
        var events = slice.get().map(eventEntityMapper::toDomain).toList();
        var date = slice.getContent().getLast().getCreatedAt();
        var lastId = slice.getContent().getLast().getId();
        String nextCursor = PageAux.encodeCursor(date, lastId);
        return new CursorPageResult<>(
                events,
                nextCursor,
                slice.hasNext()
        );
    }

    @Override
    public void deleteByIdAndCreatorId(String id, String creatorId) {
        jpaEventRepository.deleteByIdIsAndCreatorId(UUID.fromString(id), UUID.fromString(creatorId));
    }
}
