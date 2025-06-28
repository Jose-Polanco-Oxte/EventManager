package jpolanco.springbootapp.event.infrastructure.services.implementations;

import jpolanco.springbootapp.event.application.ports.input.request.CursorPaginationRequest;
import jpolanco.springbootapp.event.application.ports.input.request.PagePaginationRequest;
import jpolanco.springbootapp.event.application.uc.unique.GetOwnEventsUC;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response.EventResponse;
import jpolanco.springbootapp.event.infrastructure.adapters.mappers.dto.EventDtoCreator;
import jpolanco.springbootapp.event.infrastructure.services.interfaces.OwnEventQueryService;
import jpolanco.springbootapp.shared.infrastructure.dto.response.CursorPageResponse;
import jpolanco.springbootapp.shared.infrastructure.dto.response.SlicePageResponse;
import jpolanco.springbootapp.shared.infrastructure.mappers.CursorPageCreator;
import jpolanco.springbootapp.shared.infrastructure.mappers.SlicePageCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OwnEventQueryServiceImpl implements OwnEventQueryService {
    private final GetOwnEventsUC getOwnEventsUC;
    private final EventDtoCreator eventDtoCreator;
    private final SlicePageCreator<Event, EventResponse> slicePageCreator;
    private final CursorPageCreator<Event, EventResponse, String> cursorPageCreator;

    @Override
    public SlicePageResponse<EventResponse> getEventsByPages(String creatorId, int page, int size, String sortBy, String sortOrder) {
        var pages = getOwnEventsUC.get(new PagePaginationRequest(page, size, sortBy, sortOrder), creatorId);
        return slicePageCreator.create(pages, eventDtoCreator);
    }

    @Override
    public CursorPageResponse<EventResponse, String> getEventsByCursorBased(String creatorId, String cursor, int size, String sortBy, String sortOrder) {
        var cursorPage = getOwnEventsUC.get(new CursorPaginationRequest<>(cursor, size, sortBy, sortOrder), creatorId);
        return cursorPageCreator.create(cursorPage, eventDtoCreator);
    }

    @Override
    public List<EventResponse> getEventsByCategory(String creatorId, String category, String modality, int page, int size) {
        return List.of();
    }

    @Override
    public List<EventResponse> getEventsByTitle(String creatorId, String title, String modality, int page, int size) {
        return List.of();
    }

    @Override
    public List<EventResponse> getEventsByLocation(String creatorId, String locationName, String modality, int page, int size) {
        return List.of();
    }
}
