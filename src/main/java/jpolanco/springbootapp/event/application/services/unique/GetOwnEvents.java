package jpolanco.springbootapp.event.application.services.unique;

import jpolanco.springbootapp.event.application.ports.input.request.CursorPaginationRequest;
import jpolanco.springbootapp.event.application.ports.input.request.PagePaginationRequest;
import jpolanco.springbootapp.event.application.ports.output.EventRepository;
import jpolanco.springbootapp.event.application.uc.unique.GetOwnEventsUC;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.utils.CursorPageResult;
import jpolanco.springbootapp.shared.utils.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetOwnEvents implements GetOwnEventsUC {
    private final EventRepository eventRepository;

    @Override
    public PageResult<Event> get(PagePaginationRequest pagePaginationRequest, String creatorId) {
        return eventRepository.findByCreatorId(
                creatorId,
                pagePaginationRequest.page(),
                pagePaginationRequest.size(),
                pagePaginationRequest.sortBy(),
                pagePaginationRequest.orderBy()
        );
    }

    @Override
    public CursorPageResult<Event, String> get(CursorPaginationRequest<String> cursorPaginationRequest, String creatorId) {
        return eventRepository.findByCreatorId(
                creatorId,
                cursorPaginationRequest.cursor(),
                cursorPaginationRequest.size(),
                cursorPaginationRequest.sortBy(),
                cursorPaginationRequest.orderBy()
        );
    }
}
