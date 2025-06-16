package jpolanco.springbootapp.shared.infrastructure.controllers;

import jpolanco.springbootapp.event.infrastructure.components.EventSortField;
import jpolanco.springbootapp.event.infrastructure.services.interfaces.EventService;
import jpolanco.springbootapp.shared.infrastructure.OrderField;
import jpolanco.springbootapp.shared.infrastructure.services.interfaces.PublicSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/public")
public class PublicController {
    // This controller can be used to define public endpoints that do not require authentication
    // For example, you can add endpoints for public search, public events, etc.
    // Currently, it does not have any specific methods defined.

    // You can add methods here as needed for public access, such as:
    // - Public search endpoints
    // - Public event listings
    // - Any other public resources

    private final EventService eventService;
    private final PublicSearchService publicSearchService;

    @GetMapping("/events/by-pages")
    public ResponseEntity<Object> getPublicEventsByPages(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "NONE", required = false) EventSortField sortBy,
            @RequestParam(defaultValue = "NONE", required = false) OrderField orderBy
    ) {
        page = Math.max(page, 0);
        size = Math.max(size, 1);
        var maybeEvents = eventService.getPublicEvents(page, size, sortBy.getField(), orderBy.getValue());
        if (maybeEvents.content().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(maybeEvents);
    }

    @GetMapping("/events/by-cursor")
    public ResponseEntity<Object> getPublicEventsByCursor(
            @RequestParam(defaultValue = "NONE", required = false) String cursor,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "NONE", required = false) EventSortField sortBy,
            @RequestParam(defaultValue = "NONE", required = false) OrderField orderBy
    ) {
        size = Math.max(size, 1);
        var maybeEvents = eventService.getPublicEvents(cursor, size, sortBy.getField(), orderBy.getValue());
        if (maybeEvents.items().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(maybeEvents);
    }

    @GetMapping("/search/events/by-name")
    public ResponseEntity<Object> searchPublicEventsByName(
            @RequestParam String name,
            @RequestParam (defaultValue = "10", required = false) int size
    ) {
        size = Math.max(size, 1);
        var maybeEvents = publicSearchService.searchPublicEventsByName(name, size);
        if (maybeEvents.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(maybeEvents);
    }
}
