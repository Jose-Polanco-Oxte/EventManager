package jpolanco.springbootapp.event.application.services;

import jpolanco.springbootapp.event.application.errors.EventAppError;
import jpolanco.springbootapp.event.application.ports.output.EventRepository;
import jpolanco.springbootapp.event.application.uc.DeleteMyEventByIdUC;
import jpolanco.springbootapp.event.infrastructure.services.interfaces.ImageStorageService;
import jpolanco.springbootapp.shared.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DeleteMyEventById implements DeleteMyEventByIdUC {
    private final EventRepository eventRepository;
    private final ImageStorageService imageStorageService;
    @Override
    public Result<Void> delete(String creatorId, String eventId) {
        var event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            return Result.failure(EventAppError.EVENT_NOT_FOUND);
        }
        if (!event.get().getCreatorId().equals(creatorId)) {
            return Result.failure(EventAppError.EVENT_NOT_BELONG_TO_USER);
        }
        eventRepository.deleteByIdAndCreatorId(eventId, creatorId);
        if (!imageStorageService.deleteImage(event.get().getPictureFileName()))
            throw new RuntimeException("Failed to delete event image: " + event.get().getPictureFileName());
        return Result.success();
    }
}
