package jpolanco.springbootapp.event.application.ports.input;

import java.util.List;

public record CreationEventHolder(
        String title,
        String description,
        String schedule,
        double latitude,
        double longitude,
        List<String> categories,
        boolean isPublic,
        boolean enableComments,
        String modality,
        List<StaffHolder> staffs
) {
}
