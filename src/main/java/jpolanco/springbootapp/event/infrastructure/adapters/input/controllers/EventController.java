package jpolanco.springbootapp.event.infrastructure.adapters.input.controllers;

import jakarta.validation.Valid;
import jpolanco.springbootapp.config.auth.MyUserDetails;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.EventCreationDto;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.UpdateEventDto;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response.EventResponseDto;
import jpolanco.springbootapp.event.infrastructure.services.interfaces.EventService;
import jpolanco.springbootapp.event.infrastructure.services.interfaces.ImageStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static jpolanco.springbootapp.event.infrastructure.adapters.input.controllers.MyEventsController.createEventResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;
    private final ImageStorageService imageStorageService;

    @PostMapping("/create")
    public ResponseEntity<Object> createEvent(
            @Valid @RequestPart("data") EventCreationDto eventCreationDto,
            @RequestPart("image") MultipartFile image,
            @AuthenticationPrincipal MyUserDetails myUserDetails
    ) {
        return createEventResponse(eventCreationDto, image, myUserDetails, eventService, imageStorageService);
    }

    @PutMapping("/update/{eventId}")
    public ResponseEntity<Object> updateEvent(
            @PathVariable String eventId,
            @Valid @RequestPart("data") UpdateEventDto eventUpdateDto,
            @RequestPart("image") MultipartFile image,
            @AuthenticationPrincipal MyUserDetails myUserDetails
    ) {
//        var result = eventService.updateEvent(eventId, eventUpdateDto, imageFileName);
//        if (result.isFailure()) {
//            return ResponseEntity.badRequest().body(result.getMessage());
//        }
//        var event = (EventResponseDto) result.getValue();
//        if (!imageStorageService.imageExists(event)) {
//            imageStorageService.storeImage(imageFileName, image);
//        }
//        return ResponseEntity.ok(result.getValue());
        return null;
    }
}
