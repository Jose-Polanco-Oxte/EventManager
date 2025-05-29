package jpolanco.springbootapp.event.application.ports.input;

import java.util.List;

public record EventHolder(
        String eventId,
        String title,
        String description,
        String schedule,
        String latitude,
        String longitude,
        String status,
        List<String> categories,
        boolean isPublic,
        boolean enableComments,
        String modality,
        List<StaffHolder> staffs,
        String pictureFileName,
        String creatorId,
        String createdAt
) {
}
