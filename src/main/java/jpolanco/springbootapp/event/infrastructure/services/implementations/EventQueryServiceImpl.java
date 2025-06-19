package jpolanco.springbootapp.event.infrastructure.services.implementations;

import jpolanco.springbootapp.event.application.ports.input.request.CursorPaginationRequest;
import jpolanco.springbootapp.event.application.ports.input.request.PagePaginationRequest;
import jpolanco.springbootapp.event.application.uc.unique.GetAllEventsUC;
import jpolanco.springbootapp.event.application.uc.unique.GetEventByIdUC;
import jpolanco.springbootapp.event.application.uc.unique.GetEventsUC;
import jpolanco.springbootapp.event.application.uc.unique.GetPublicEventsUC;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response.EventResponse;
import jpolanco.springbootapp.event.infrastructure.adapters.mappers.dto.EventDtoCreator;
import jpolanco.springbootapp.event.infrastructure.services.interfaces.EventQueryService;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.infrastructure.dto.CursorPageResponseDto;
import jpolanco.springbootapp.shared.infrastructure.dto.SlicePageResponseDto;
import jpolanco.springbootapp.shared.infrastructure.mappers.CursorPageCreator;
import jpolanco.springbootapp.shared.infrastructure.mappers.SlicePageCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EventQueryServiceImpl implements EventQueryService {
    private final GetEventByIdUC getEventByIdUC;
    private final GetAllEventsUC getAllEventsUC;
    private final GetEventsUC getEventsUC;
    private final GetPublicEventsUC getPublicEventsUC;
    private final EventDtoCreator eventDtoCreator;
    private final SlicePageCreator<Event, EventResponse> slicePageCreator;
    private final CursorPageCreator<Event, EventResponse, String> cursorPageCreator;

    @Override
    public Optional<EventResponse> getEventById(String eventId) {
        var maybeEvent = getEventByIdUC.get(eventId);
        if (maybeEvent.isFailure()) {
            return Optional.empty();
        }
        var event = maybeEvent.getValue();
        return Optional.of(eventDtoCreator.create(event));
    }

    @Override
    public List<EventResponse> getAllEvents() {
        var events = getAllEventsUC.get();
        if (events.isEmpty()) {
            return List.of();
        }
        return events.stream()
                .map(eventDtoCreator::create)
                .toList();
    }

    @Override
    public SlicePageResponseDto<EventResponse> getEventsByPages(int page, int size, String sortBy, String sortOrder) {
        var pages = getEventsUC.get(new PagePaginationRequest(page, size, sortBy, sortOrder));
        return slicePageCreator.create(pages, eventDtoCreator);
    }

    @Override
    public CursorPageResponseDto<EventResponse, String> getEventsByCursorBased(String cursor, int size, String sortBy, String sortOrder) {
        var cursorPage = getEventsUC.get(new CursorPaginationRequest<>(cursor, size, sortBy, sortOrder));
        return cursorPageCreator.create(cursorPage, eventDtoCreator);
    }

    @Override
    public SlicePageResponseDto<EventResponse> getPublicEventsByPages(int page, int size, String sortBy, String sortOrder) {
        var pages = getPublicEventsUC.get(new PagePaginationRequest(page, size, sortBy, sortOrder));
        return slicePageCreator.create(pages, eventDtoCreator);
    }

    @Override
    public CursorPageResponseDto<EventResponse, String> getPublicEventsByCursorBased(String cursor, int size, String sortBy, String sortOrder) {
        var cursorPage = getPublicEventsUC.get(new CursorPaginationRequest<>(cursor, size, sortBy, sortOrder));
        return cursorPageCreator.create(cursorPage, eventDtoCreator);
    }
}