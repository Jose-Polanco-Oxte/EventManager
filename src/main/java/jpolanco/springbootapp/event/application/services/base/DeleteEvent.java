package jpolanco.springbootapp.event.application.services.base;

import jpolanco.springbootapp.event.application.application_events.EventDeleted;
import jpolanco.springbootapp.event.application.errors.EventAppError;
import jpolanco.springbootapp.event.application.ports.input.providers.FileStorageProvider;
import jpolanco.springbootapp.event.application.ports.output.EventCommandRepository;
import jpolanco.springbootapp.event.application.ports.output.EventQueryRepository;
import jpolanco.springbootapp.event.application.uc.base.DeleteEventUC;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeleteEvent implements DeleteEventUC {
    private final EventQueryRepository queryRepository;
    private final EventCommandRepository commandRepository;
    private final FileStorageProvider fileStorage;

    @Override
    public Result<List<EventNotification>> delete(Event event, String reason) {
        if (event == null) {
            return Result.failure(EventAppError.EVENT_NOT_FOUND);
        }
        commandRepository.deleteById(event.getEventId());
        if (!fileStorage.deleteImage(event.getPictureFileName())) {
            return Result.failure(EventAppError.IMAGE_DELETE_ERROR);
        }
        event.recordEvent(new EventDeleted(event.getEventId(), event.getTitle(), reason));
        return Result.success(event.pullEvents());
    }

    @Override
    public Result<List<EventNotification>> deleteById(String eventId, String reason) {
        var maybeEvent = queryRepository.findById(eventId);
        if (maybeEvent.isEmpty()) {
            return Result.failure(EventAppError.EVENT_NOT_FOUND);
        }
        var event = maybeEvent.get();
        var result = delete(event, reason);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        var domainEvents = result.getValue();
        return Result.success(domainEvents);
    }
}
