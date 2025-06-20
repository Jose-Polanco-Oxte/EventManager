package jpolanco.springbootapp.event.infrastructure.adapters.input.controllers;

import jakarta.validation.constraints.Min;
import jpolanco.springbootapp.event.infrastructure.components.utils.EventSortField;
import jpolanco.springbootapp.event.infrastructure.services.interfaces.EventQueryService;
import jpolanco.springbootapp.event.infrastructure.services.interfaces.SearchEventService;
import jpolanco.springbootapp.shared.infrastructure.controllers.ResponseHandler;
import jpolanco.springbootapp.shared.utils.OrderField;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(value = "/public/events")
public class PublicEventController {

    private final SearchEventService searchEventService;
    private final EventQueryService queryService;

    @GetMapping("/search")
    public ResponseEntity<Object> searchEvent(
            @RequestParam() String query,
            @Min(1) @RequestParam(defaultValue = "10", required = false) int size
    ) {
        var result = searchEventService.searchPublicEventsByName(query, size);
        if (result.isEmpty()) {
            return ResponseHandler.noContent();
        }
        return ResponseHandler.ok(result);
    }

    @GetMapping("/pages")
    public ResponseEntity<Object> getEventsByPages(
            @Min(0) @RequestParam(defaultValue = "0", required = false) int page,
            @Min(1) @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "NONE", required = false) EventSortField sortBy,
            @RequestParam(defaultValue = "NONE", required = false) OrderField order
    ) {
        var events = queryService.getPublicEventsByPages(page, size, sortBy.getValue(), order.getValue());
        if (events.content().isEmpty()) {
            return ResponseHandler.noContent();
        }
        return ResponseHandler.ok(events);
    }

    @GetMapping("/cursor")
    public ResponseEntity<Object> getEventsByCursor(
            @RequestParam(defaultValue = "NONE", required = false) String cursor,
            @Min(1) @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "NONE", required = false) EventSortField sortBy,
            @RequestParam(defaultValue = "NONE", required = false) OrderField orderBy
    ) {
        var events = queryService.getPublicEventsByCursorBased(cursor, size, sortBy.getValue(), orderBy.getValue());
        if (events.items().isEmpty()) {
            return ResponseHandler.noContent();
        }
        return ResponseHandler.ok(events);
    }
}
