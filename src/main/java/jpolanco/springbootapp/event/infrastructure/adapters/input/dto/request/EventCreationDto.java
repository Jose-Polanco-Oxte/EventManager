package jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request;

import jpolanco.springbootapp.event.application.ports.input.StaffHolder;

import java.util.List;

public record EventCreationDto(
        String title,
        String description,
        String schedule,
        long durationInSeconds,
        double latitude,
        double longitude,
        List<String> categories,
        boolean isPublic,
        boolean enableComments,
        String modality,
        List<StaffHolder> staffs
) {
}
