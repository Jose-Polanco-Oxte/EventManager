package jpolanco.springbootapp.event.infrastructure.adapters.input.controllers;

import jakarta.validation.Valid;
import jpolanco.springbootapp.config.auth.MyUserDetails;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.EventCreationDto;
import jpolanco.springbootapp.event.infrastructure.services.interfaces.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/my-events")
public class MyEventsController {

    private final EventService eventService;
    @Value("${IMAGEPATH}")
    private String IMAGE_DIRECTORY;

    @PostMapping("/create")
    public ResponseEntity<Object> createEvent(
            @Valid @RequestPart("data") EventCreationDto eventCreationDto,
            @RequestPart("image")MultipartFile image,
            @AuthenticationPrincipal MyUserDetails myUserDetails
            ) throws IOException {
        String imageFileName = UUID.randomUUID() + "-" + image.getOriginalFilename();
        var response = eventService.createEvent(eventCreationDto, myUserDetails.getId(), imageFileName);
        if (response.isFailure()) {
            return ResponseEntity.badRequest().body(response.getMessage());
        }
        Path path = Paths.get(IMAGE_DIRECTORY).resolve(imageFileName);
        Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return ResponseEntity.ok(response.getValue());
    }

    @PutMapping("/update/{eventId}")
    public ResponseEntity<Object> updateEvent(@PathVariable String eventId){
        return null;
    }

    @GetMapping("/get/{eventId}")
    public ResponseEntity<Object> getEvent(@PathVariable String eventId) {
        return null;
    }

    @GetMapping("/get-all")
    public ResponseEntity<Object> getAllEvents(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        return null;
    }

    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<Object> deleteEvent(@PathVariable String eventId) {
        return null;
    }
}