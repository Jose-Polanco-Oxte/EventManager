package jpolanco.springbootapp.event.application.services.base;

import jpolanco.springbootapp.event.application.errors.EventAppError;
import jpolanco.springbootapp.event.application.ports.input.providers.FileStorageProvider;
import jpolanco.springbootapp.event.application.ports.output.EventCommandRepository;
import jpolanco.springbootapp.event.application.ports.output.EventQueryRepository;
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

@Service
@RequiredArgsConstructor
public class UpdateEvent implements UpdateEventUC {
    private final EventQueryRepository queryRepository;
    private final EventCommandRepository commandRepository;
    private final EventValidation eventValidation;
    private final FileStorageProvider fileStorageProvider;

    @Override
    public Result<Event> setChanges(Event event, UpdateEventRequest request, InputStream inputStream) {
        var updater = EventUpdater.updater(event, fileStorageProvider, eventValidation)
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
        var savedEvent = commandRepository.save(updatedEvent.getValue());
        return Result.success(savedEvent);
    }

    @Override
    public Result<Event> setChangesById(String eventId, UpdateEventRequest request, InputStream inputStream) {
        var maybeEvent = queryRepository.findById(eventId);
        if (maybeEvent.isEmpty()) {
            return Result.failure(EventAppError.EVENT_NOT_FOUND);
        }
        var event = maybeEvent.get();
        return setChanges(event, request, inputStream);
    }
}
