package jpolanco.springbootapp.event.infrastructure.adapters.input.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jpolanco.springbootapp.config.auth.MyUserDetails;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.EventCreationRequest;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.UpdateEventRequest;
import jpolanco.springbootapp.event.infrastructure.adapters.input.validations.anottations.ValidUUID;
import jpolanco.springbootapp.event.infrastructure.components.utils.EventSortField;
import jpolanco.springbootapp.event.infrastructure.services.interfaces.EventCommandService;
import jpolanco.springbootapp.event.infrastructure.services.interfaces.EventQueryService;
import jpolanco.springbootapp.shared.infrastructure.controllers.ResponseHandler;
import jpolanco.springbootapp.shared.utils.OrderField;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(value = "/events")
public class EventController {

    private final EventCommandService commandService;
    private final EventQueryService queryService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    @PostMapping()
    public ResponseEntity<Object> createEvent(
            @Valid @RequestPart("data") EventCreationRequest eventCreationRequest,
            @RequestPart("image") MultipartFile image,
            @AuthenticationPrincipal MyUserDetails myUserDetails
    ) throws IOException {
        return createEventResponse(
                myUserDetails,
                eventCreationRequest,
                image,
                commandService
        );
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Object> getEventById(
            @ValidUUID @PathVariable String eventId
    ) {
        var response = queryService.getEventById(eventId);
        return response.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(ResponseHandler::notFound);
    }

    @GetMapping("/pages")
    public ResponseEntity<Object> getEventsByPages(
            @Min(0) @RequestParam(defaultValue = "0", required = false) int page,
            @Min(1) @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "NONE", required = false) EventSortField sortBy,
            @RequestParam(defaultValue = "NONE", required = false) OrderField order
    ) {
        var events = queryService.getEventsByPages(
                page,
                size,
                sortBy.getValue(),
                order.getValue()
        );
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
            @RequestParam(defaultValue = "NONE", required = false) OrderField order
    ) {
        var events = queryService.getEventsByCursorBased(
                cursor,
                size,
                sortBy.getValue(),
                order.getValue()
        );
        if (events.items().isEmpty()) {
            return ResponseHandler.noContent();
        }
        return ResponseHandler.ok(events);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{eventId}")
    public ResponseEntity<Object> updateEvent(
            @ValidUUID @PathVariable String eventId,
            @Valid @RequestPart("data") UpdateEventRequest eventUpdateDto,
            @RequestPart("image") MultipartFile image
    ) throws IOException {
        var commandResult = commandService.updateEvent(eventId, eventUpdateDto, image.getInputStream());
        if (commandResult.isFailure()) {
            return ResponseHandler.error(commandResult.getMessage(), commandResult.getErrorCode());
        }
        return ResponseHandler.ok("Event updated successfully");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Object> deleteEvent(
            @ValidUUID @PathVariable String eventId
    ) {
        var commandResult = commandService.deleteEventById(eventId);
        if (commandResult.isFailure()) {
            return ResponseHandler.error(commandResult.getMessage(), commandResult.getErrorCode());
        }
        return ResponseHandler.ok("Event deleted successfully");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{eventId}/cancel")
    public ResponseEntity<Object> cancelEvent(
            @PathVariable String eventId,
            @RequestParam String reason
    ) {
        var commandResult = commandService.cancelEvent(eventId, reason);
        if (commandResult.isFailure()) {
            return ResponseHandler.error(commandResult.getMessage(), commandResult.getErrorCode());
        }
        return ResponseHandler.ok("Event cancelled successfully");
    }

    private ResponseEntity<Object> createEventResponse(
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            @RequestPart("data") @Valid EventCreationRequest request,
            @RequestPart("image") MultipartFile image,
            EventCommandService commandService
    ) throws IOException {
        var commandResult = commandService.createEvent(myUserDetails.getId(), request, image.getInputStream());
        if (commandResult.isFailure()) {
            return ResponseHandler.error(commandResult.getMessage(), commandResult.getErrorCode());
        }
        return ResponseHandler.created("Event created successfully");
    }
}
