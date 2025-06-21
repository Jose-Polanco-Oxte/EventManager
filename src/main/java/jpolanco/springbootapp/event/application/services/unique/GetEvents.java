package jpolanco.springbootapp.event.application.services.unique;

import jpolanco.springbootapp.event.application.ports.input.request.CursorPaginationRequest;
import jpolanco.springbootapp.event.application.ports.input.request.PagePaginationRequest;
import jpolanco.springbootapp.event.application.ports.output.EventQueryRepository;
import jpolanco.springbootapp.event.application.uc.unique.GetEventsUC;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.utils.CursorPageResult;
import jpolanco.springbootapp.shared.utils.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetEvents implements GetEventsUC {
    private final EventQueryRepository queryRepository;
    @Override
    public PageResult<Event> get(PagePaginationRequest pagePaginationRequest) {
        return queryRepository.findAll(
                pagePaginationRequest.page(),
                pagePaginationRequest.size(),
                pagePaginationRequest.sortBy(),
                pagePaginationRequest.orderBy());
    }

    @Override
    public CursorPageResult<Event, String> get(CursorPaginationRequest<String> cursorPaginationRequest) {
        return queryRepository.findAll(
                cursorPaginationRequest.cursor(),
                cursorPaginationRequest.size(),
                cursorPaginationRequest.sortBy(),
                cursorPaginationRequest.orderBy());
    }
}
