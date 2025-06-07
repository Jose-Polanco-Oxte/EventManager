package jpolanco.springbootapp.event.application.services;

import jpolanco.springbootapp.event.application.errors.EventAppError;
import jpolanco.springbootapp.event.application.errors.EventPersistenceFailure;
import jpolanco.springbootapp.event.application.ports.output.EventRepository;
import jpolanco.springbootapp.event.application.uc.DeleteEventByIdUC;
import jpolanco.springbootapp.event.infrastructure.services.interfaces.ImageStorageService;
import jpolanco.springbootapp.shared.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DeleteEventById implements DeleteEventByIdUC {
    private final EventRepository eventRepository;
    private final ImageStorageService imageStorageService;
    @Override
    public Result<Void> deleteById(String eventId) {
        var event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            return Result.failure(EventAppError.EVENT_NOT_FOUND);
        }
        eventRepository.deleteById(eventId);
        if (!imageStorageService.deleteImage(event.get().getPictureFileName()))
            throw new EventPersistenceFailure("Failed to delete event image");
        return Result.success();
    }
}
