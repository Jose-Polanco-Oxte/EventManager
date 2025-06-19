package jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jpolanco.springbootapp.event.application.ports.input.request.StaffRequest;

import java.time.Instant;
import java.util.List;

public record EventCreationRequest(
        @NotBlank(message = "Title cannot be blank")
        String title,
        String description,
        @NotBlank(message = "Schedule cannot be blank")
        @NotNull(message = "Schedule cannot be null")
        Instant schedule,
        @NotNull(message = "Duration cannot be null")
        long durationInSeconds,
        String locationName,
        @NotBlank(message = "Location city cannot be blank")
        String locationCity,
        @NotBlank(message = "Location country cannot be blank")
        String locationCountry,
        double latitude,
        double longitude,
        @NotNull(message = "Categories cannot be null")
        List<String> categories,
        @NotNull(message = "Public option cannot be null")
        boolean isPublic,
        @NotNull(message = "Enable comments option cannot be null")
        boolean enableComments,
        @NotBlank(message = "Modality cannot be blank")
        String modality,
        List<StaffRequest> staffs,
        @NotNull(message = "Max assistance count cannot be null")
        int maxAttendees
) {
}
