package jpolanco.springbootapp.event.infrastructure.adapters.input.controllers;

import jakarta.validation.Valid;
import jpolanco.springbootapp.config.auth.MyUserDetails;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.EventCreationDto;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.UpdateEventDto;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response.EventResponseDto;
import jpolanco.springbootapp.event.infrastructure.components.EventSortField;
import jpolanco.springbootapp.event.infrastructure.services.interfaces.EventService;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.infrastructure.OrderField;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static jpolanco.springbootapp.event.infrastructure.adapters.input.controllers.MyEventsController.createEventResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    @PostMapping("/create")
    public ResponseEntity<Object> createEvent(
            @Valid @RequestPart("data") EventCreationDto eventCreationDto,
            @RequestPart("image") MultipartFile image,
            @AuthenticationPrincipal MyUserDetails myUserDetails
    ) throws IOException {
        return createEventResponse(myUserDetails, eventCreationDto, image, eventService);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Object> getEventById(
            @PathVariable String eventId
    ) {
        Result<EventResponseDto> response = eventService.getEventById(eventId);
        if (response.isFailure()) {
            return ResponseEntity.badRequest().body(response.getMessage());
        }
        return ResponseEntity.ok(response.getValue());
    }

    @GetMapping("/get-all")
    public ResponseEntity<Object> getAllEvents() {
        var response = eventService.getAllEvents();
        if (response.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-events/by-pages")
    public ResponseEntity<Object> getMyEventsByPages(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "NONE", required = false) EventSortField sortBy,
            @RequestParam(defaultValue = "NONE", required = false) OrderField order
    ) {
        page = Math.max(page, 0);
        size = Math.max(size, 1);
        var maybeEvents = eventService.getEvents(page, size, sortBy.getField(), order.getValue());
        if (maybeEvents.content().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(maybeEvents);
    }

    @GetMapping("/get-events/by-cursor")
    public ResponseEntity<Object> getEventsByCursor(
            @RequestParam(defaultValue = "NONE", required = false) String cursor,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "NONE", required = false) EventSortField sortBy,
            @RequestParam(defaultValue = "NONE", required = false) OrderField order
    ) {
        size = Math.max(size, 1);
        var maybeEvents = eventService.getEvents(cursor, size, sortBy.getField(), order.getValue());
        if (maybeEvents.items().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(maybeEvents);
    }

    @PutMapping("/update/{eventId}")
    public ResponseEntity<Object> updateEvent(
            @PathVariable String eventId,
            @Valid @RequestPart("data") UpdateEventDto eventUpdateDto,
            @RequestPart("image") MultipartFile image,
            @RequestParam(required = false) String type
    ) throws IOException {
        Result<EventResponseDto> result;
        if (type.equalsIgnoreCase("addstaff")) {
            result = eventService.updateEventAddStaff(eventId, eventUpdateDto, image.getInputStream());
        } else if (type.equalsIgnoreCase("clearstaff")) {
            result = eventService.updateEventClearStaff(eventId, eventUpdateDto, image.getInputStream());
        } else {
            result = eventService.updateEvent(eventId, eventUpdateDto, image.getInputStream());
        }
        if (result.isFailure()) {
            return ResponseEntity.badRequest().body(result.getMessage());
        }
        return ResponseEntity.ok(result.getValue());
    }

    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<Object> deleteEvent(
            @PathVariable String eventId
    ) {
        var response = eventService.deleteEventById(eventId);
        if (response.isFailure()) {
            return ResponseEntity.badRequest().body(response.getMessage());
        }
        return ResponseEntity.ok(response.getValue());
    }

    @PutMapping("/cancel/{eventId}")
    public ResponseEntity<Object> cancelEvent(
            @PathVariable String eventId,
            @RequestParam String reason
    ) {
        var response = eventService.eventCancelation(eventId, reason);
        if (response.isFailure()) {
            return ResponseEntity.badRequest().body(response.getMessage());
        }
        return ResponseEntity.ok(response.getValue());
    }
}
