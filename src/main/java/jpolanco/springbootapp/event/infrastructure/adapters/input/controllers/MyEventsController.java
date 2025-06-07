package jpolanco.springbootapp.event.infrastructure.adapters.input.controllers;

import jakarta.validation.Valid;
import jpolanco.springbootapp.config.auth.MyUserDetails;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.EventCreationDto;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.UpdateEventDto;
import jpolanco.springbootapp.event.infrastructure.services.interfaces.EventService;
import jpolanco.springbootapp.event.infrastructure.services.interfaces.ImageStorageService;
import jpolanco.springbootapp.event.infrastructure.services.interfaces.MyEventsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/my-events")
public class MyEventsController {

    private final ImageStorageService imageStorageService;
    private final EventService eventService;
    private final MyEventsService myEventsService;

    @PostMapping("/create")
    public ResponseEntity<Object> createEvent(
            @Valid @RequestPart("data") EventCreationDto eventCreationDto,
            @RequestPart("image")MultipartFile image,
            @AuthenticationPrincipal MyUserDetails myUserDetails
            ){

        return createEventResponse(eventCreationDto, image, myUserDetails, eventService, imageStorageService);
    }

    @PutMapping("/update/{eventId}")
    public ResponseEntity<Object> updateEvent(
            @PathVariable String eventId,
            @Valid @RequestPart("data") UpdateEventDto eventUpdateDto,
            @RequestPart ("image") MultipartFile image,
            @AuthenticationPrincipal MyUserDetails myUserDetails
    ){
        String imageFileName = UUID.randomUUID() + "-" + image.getOriginalFilename();
        var result = myEventsService.updateMyEvent(myUserDetails.getId(), eventId, imageFileName, eventUpdateDto);
        if (result.isFailure()) {
            return ResponseEntity.badRequest().body(result.getMessage());
        }
        if (!imageStorageService.imageExists(imageFileName)) {
            imageStorageService.storeImage(imageFileName, image);
        }
        return ResponseEntity.ok(result.getValue());
    }

    @GetMapping("/get-all")
    public ResponseEntity<Object> getAllMyEvents(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        var response = myEventsService.getMyEvents(myUserDetails.getId(), 0, 10);
        if (response.isFailure()) {
            return ResponseEntity.badRequest().body(response.getMessage());
        }
        return ResponseEntity.ok(response.getValue());
    }

    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<Object> deleteEvent(
            @PathVariable String eventId,
            @AuthenticationPrincipal MyUserDetails myUserDetails
    ) {
        var response = myEventsService.deleteMyEvent(myUserDetails.getId(), eventId);
        if (response.isFailure()) {
            return ResponseEntity.badRequest().body(response.getMessage());
        }
        return ResponseEntity.ok("Event deleted successfully");
    }


    static ResponseEntity<Object> createEventResponse(@RequestPart("data") @Valid EventCreationDto eventCreationDto, @RequestPart("image") MultipartFile image, @AuthenticationPrincipal MyUserDetails myUserDetails, EventService eventService, ImageStorageService imageStorageService) {
        String imageFileName = UUID.randomUUID() + "-" + image.getOriginalFilename();
        var response = eventService.createEvent(eventCreationDto, myUserDetails.getId(), imageFileName);
        if (response.isFailure()) {
            return ResponseEntity.badRequest().body(response.getMessage());
        }
        var store = imageStorageService.storeImage(imageFileName, image);
        if (!store) {
            return ResponseEntity.status(500).body("Failed to store image");
        }
        return ResponseEntity.ok(response.getValue());
    }
}