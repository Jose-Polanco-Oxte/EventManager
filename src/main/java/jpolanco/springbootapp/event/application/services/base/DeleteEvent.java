package jpolanco.springbootapp.event.application.services.base;

import jpolanco.springbootapp.event.application.errors.EventAppError;
import jpolanco.springbootapp.event.application.ports.input.providers.FileStorageProvider;
import jpolanco.springbootapp.event.application.ports.output.EventRepository;
import jpolanco.springbootapp.event.application.uc.base.DeleteEventUC;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteEvent implements DeleteEventUC {
    private final EventRepository eventRepository;
    private final FileStorageProvider fileStorage;

    @Override
    public Result<Void> delete(Event event) {
        if (event == null) {
            return Result.failure(EventAppError.EVENT_NOT_FOUND);
        }
        eventRepository.deleteById(event.getEventId());
        if (!fileStorage.deleteImage(event.getPictureFileName())) {
            return Result.failure(EventAppError.IMAGE_DELETE_ERROR);
        }
        return Result.success();
    }

    @Override
    public Result<Void> deleteById(String eventId) {
        var event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            return Result.failure(EventAppError.EVENT_NOT_FOUND);
        }
        eventRepository.deleteById(eventId);
        if (!fileStorage.deleteImage(event.get().getPictureFileName()))
            return Result.failure(EventAppError.IMAGE_DELETE_ERROR);
        return Result.success();
    }
}
