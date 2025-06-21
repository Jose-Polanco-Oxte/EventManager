package jpolanco.springbootapp.event.infrastructure.adapters.input.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jpolanco.springbootapp.config.auth.MyUserDetails;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.CommandReasonRequest;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.UpdateEventRequest;
import jpolanco.springbootapp.event.infrastructure.adapters.input.validations.annotations.ValidUUID;
import jpolanco.springbootapp.event.infrastructure.components.utils.EventSortField;
import jpolanco.springbootapp.event.infrastructure.services.interfaces.OwnEventCommandService;
import jpolanco.springbootapp.event.infrastructure.services.interfaces.OwnEventQueryService;
import jpolanco.springbootapp.event.infrastructure.services.interfaces.SearchEventService;
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
@PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
@RequestMapping("/my-events")
public class MyEventsController {

    private final OwnEventCommandService ownCommandService;
    private final OwnEventQueryService ownQueryService;
    private final SearchEventService searchEventService;

    @PutMapping("/{eventId}")
    public ResponseEntity<Object> updateEvent(
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            @ValidUUID @PathVariable String eventId,
            @Valid @RequestPart("data") UpdateEventRequest eventUpdateDto,
            @RequestPart ("image") MultipartFile image
    ) throws IOException {
        var commandResult = ownCommandService.updateEvent(
                myUserDetails.getId(),
                eventId,
                eventUpdateDto,
                image.getInputStream()
        );
        if (commandResult.isFailure()) {
            return ResponseHandler.error(commandResult.getMessage(), commandResult.getErrorCode());
        }
        return ResponseHandler.ok("Event updated successfully");
    }

    @GetMapping("/pages")
    public ResponseEntity<Object> getMyEventsByPages(
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            @Min(0) @RequestParam(defaultValue = "0", required = false) int page,
            @Min(1) @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "NONE", required = false) EventSortField sortBy,
            @RequestParam(defaultValue = "NONE", required = false) OrderField orderBy
    ) {
        var response = ownQueryService.getEventsByPages(
                myUserDetails.getId(),
                page,
                size,
                sortBy.getValue(),
                orderBy.getValue()
        );
        if (response.content().isEmpty()) {
            return ResponseHandler.noContent();
        }
        return ResponseHandler.ok(response);
    }

    @GetMapping("/cursor")
    public ResponseEntity<Object> getEventsByCursor(
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            @RequestParam(defaultValue = "NONE", required = false) String cursor,
            @Min(1) @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "NONE", required = false) EventSortField sortBy,
            @RequestParam(defaultValue = "NONE", required = false) OrderField orderBy
    ) {
        var response = ownQueryService.getEventsByCursorBased(
                myUserDetails.getId(),
                cursor,
                size,
                sortBy.getValue(),
                orderBy.getValue()
        );
        if (response.items().isEmpty()) {
            return ResponseHandler.noContent();
        }
        return ResponseHandler.ok(response);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Object> deleteEvent(
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            @ValidUUID @PathVariable String eventId,
            @Valid @RequestBody CommandReasonRequest request
            ) {
        var commandResult = ownCommandService.deleteEvent(myUserDetails.getId(), eventId, request.reason());
        if (commandResult.isFailure()) {
            return ResponseHandler.error(commandResult.getMessage(), commandResult.getErrorCode());
        }
        return ResponseHandler.ok("Event deleted successfully");
    }

    @PutMapping("/{eventId}/cancel")
    public ResponseEntity<Object> cancelEvent(
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            @ValidUUID @PathVariable String eventId,
            @Valid @RequestBody CommandReasonRequest request
    ) {
        var commandResult = ownCommandService.cancelEvent(myUserDetails.getId(), eventId, request.reason());
        if (commandResult.isFailure()) {
            return ResponseHandler.error(commandResult.getMessage(), commandResult.getErrorCode());
        }
        return ResponseHandler.ok("Event cancelled successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchMyEventsByName(
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            @RequestParam String query,
            @Min(1) @RequestParam(defaultValue = "10", required = false) int size
    ) {
        var result = searchEventService.searchMyEventsByName(query, myUserDetails.getId(), size);
        if (result.isEmpty()) {
            return ResponseHandler.noContent();
        }
        return ResponseHandler.ok(result);
    }
}