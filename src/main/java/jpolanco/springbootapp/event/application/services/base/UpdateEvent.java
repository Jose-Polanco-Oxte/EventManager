package jpolanco.springbootapp.event.application.services.base;

import jpolanco.springbootapp.event.application.errors.EventAppError;
import jpolanco.springbootapp.event.application.ports.input.providers.FileStorageProvider;
import jpolanco.springbootapp.event.application.ports.output.EventRepository;
import jpolanco.springbootapp.event.application.uc.base.UpdateEventUC;
import jpolanco.springbootapp.event.application.utils.EventUpdater;
import jpolanco.springbootapp.event.application.utils.EventValidation;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.event.domain.model.valueobjects.Modality;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.UpdateEventRequest;
import jpolanco.springbootapp.shared.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@RequiredArgsConstructor
@Service
public class UpdateEvent implements UpdateEventUC {
    private final EventRepository eventRepository;
    private final EventValidation eventValidation;
    private final FileStorageProvider fileStorageProvider;

    @Override
    public Result<Event> setChanges(Event event, UpdateEventRequest request, InputStream inputStream) {
        return getEventResult(event, request, inputStream);
    }

    @Override
    public Result<Event> setChangesById(String eventId, UpdateEventRequest request, InputStream inputStream) {
        var maybeEvent = eventRepository.findById(eventId);
        if (maybeEvent.isEmpty()) {
            return Result.failure(EventAppError.EVENT_NOT_FOUND);
        }
        var event = maybeEvent.get();
        return getEventResult(event, request, inputStream);
    }


    private Result<Event> getEventResult(Event event, UpdateEventRequest request, InputStream inputStream) {
        var updater = new EventUpdater(event, fileStorageProvider, eventValidation)
                .title(request.title())
                .description(request.description())
                .schedule(request.schedule())
                .duration(request.durationInSeconds())
                .location(
                        request.locationName(),
                        request.locationCity(),
                        request.locationCountry(),
                        request.latitude(),
                        request.longitude()
                )
                .isPublic(request.isPublic())
                .enableComments(request.enableComments())
                .modality(Modality.fromString(request.modality()))
                .changePicture(inputStream)
                .setMaxAttendees(request.maxAttendees());
        updater.removeCategories(request.categories().remove());
        updater.addCategories(request.categories().add());
        if (request.staff().clear()) {
            updater.clearStaff();
        } else {
            updater.removeStaff(request.staff().remove());
            updater.addStaff(request.staff().add());
        }
        var updatedEvent = updater.update();
        if (updatedEvent.isFailure()) {
            return Result.failure(updatedEvent.getError());
        }
        var savedEvent = eventRepository.save(updatedEvent.getValue());
        return Result.success(savedEvent);
    }
}
