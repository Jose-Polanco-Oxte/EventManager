package jpolanco.springbootapp.event.infrastructure.adapters.input.controllers;

import jakarta.validation.Valid;
import jpolanco.springbootapp.config.auth.MyUserDetails;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.EventCreationDto;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.UpdateEventDto;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response.EventResponseDto;
import jpolanco.springbootapp.event.infrastructure.components.EventSortField;
import jpolanco.springbootapp.event.infrastructure.services.interfaces.EventService;
import jpolanco.springbootapp.event.infrastructure.services.interfaces.MyEventsService;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.infrastructure.OrderField;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/my-events")
public class MyEventsController {
    private final EventService eventService;
    private final MyEventsService myEventsService;

    @PostMapping("/create")
    public ResponseEntity<Object> createEvent(
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            @Valid @RequestPart("data") EventCreationDto eventCreationDto,
            @RequestPart("image")MultipartFile image
    ) throws IOException {
        return createEventResponse(myUserDetails, eventCreationDto, image, eventService);
    }

    @PutMapping("/update/{eventId}")
    public ResponseEntity<Object> updateEvent(
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            @PathVariable String eventId,
            @Valid @RequestPart("data") UpdateEventDto eventUpdateDto,
            @RequestPart ("image") MultipartFile image,
            @RequestParam() String type
    ) throws IOException {
        Result<EventResponseDto> result;
        if (type.equalsIgnoreCase("addstaff")) {
            result = myEventsService.updateMyEventAddStaff(myUserDetails.getId(), eventId, image.getInputStream(), eventUpdateDto);
        } else if (type.equalsIgnoreCase("clearstaff")) {
            result = myEventsService.updateMyEventClearStaff(myUserDetails.getId(), eventId, image.getInputStream(), eventUpdateDto);
        } else {
            result = myEventsService.updateMyEvent(myUserDetails.getId(), eventId, image.getInputStream(), eventUpdateDto);
        }
        if (result.isFailure()) {
            return ResponseEntity.badRequest().body(result.getMessage());
        }
        return ResponseEntity.ok(result.getValue());
    }

    @GetMapping("/get/by-pages")
    public ResponseEntity<Object> getMyEventsByPages(
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "NONE", required = false) EventSortField sortBy,
            @RequestParam(defaultValue = "NONE", required = false) OrderField orderBy
    ) {
        page = Math.max(page, 0);
        size = Math.max(size, 1);
        var response = myEventsService.getMyEvents(
                myUserDetails.getId(),
                page,
                size,
                sortBy.getField(),
                orderBy.getValue()
        );
        if (response.content().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/by-cursor")
    public ResponseEntity<Object> getEventsByCursor(
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            @RequestParam(defaultValue = "NONE", required = false) String cursor,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "NONE", required = false) EventSortField sortBy,
            @RequestParam(defaultValue = "NONE", required = false) OrderField orderBy
    ) {
        size = Math.max(size, 1);
        var response = myEventsService.getMyEvents(
                myUserDetails.getId(),
                cursor,
                size,
                sortBy.getField(),
                orderBy.getValue()
        );
        if (response.items().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<Object> deleteEvent(
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            @PathVariable String eventId
    ) {
        var response = myEventsService.deleteMyEvent(myUserDetails.getId(), eventId);
        if (response.isFailure()) {
            return ResponseEntity.badRequest().body(response.getMessage());
        }
        return ResponseEntity.ok("Event deleted successfully");
    }

    @PutMapping("/cancel/{eventId}")
    public ResponseEntity<Object> cancelEvent(
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            @PathVariable String eventId,
            @RequestParam String reason
    ) {
        var response = myEventsService.myEventCancelation(myUserDetails.getId(), eventId, reason);
        if (response.isFailure()) {
            return ResponseEntity.badRequest().body(response.getMessage());
        }
        return ResponseEntity.ok(response.getValue());
    }

    static ResponseEntity<Object> createEventResponse(
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            @RequestPart("data") @Valid EventCreationDto eventCreationDto,
            @RequestPart("image") MultipartFile image,
            EventService eventService
    ) throws IOException {
        var response = eventService.createEvent(eventCreationDto, myUserDetails.getId(), image.getInputStream());
        if (response.isFailure()) {
            return ResponseEntity.badRequest().body(response.getMessage());
        }
        return ResponseEntity.ok(response.getValue());
    }
}