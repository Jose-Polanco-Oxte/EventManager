package jpolanco.springbootapp.event.application.services.unique;

import jpolanco.springbootapp.event.application.errors.EventAppError;
import jpolanco.springbootapp.event.application.ports.input.providers.FileStorageProvider;
import jpolanco.springbootapp.event.application.ports.output.EventRepository;
import jpolanco.springbootapp.event.application.uc.unique.CreateEventUC;
import jpolanco.springbootapp.event.application.utils.EventValidation;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.event.domain.model.valueobjects.Modality;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.EventCreationRequest;
import jpolanco.springbootapp.shared.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateEvent implements CreateEventUC {
    private final EventRepository eventRepository;
    private final EventValidation eventValidation;
    private final FileStorageProvider fileStorage;

    @Override
    public Result<Event> create(String creatorId, EventCreationRequest request, InputStream imageStream) {
        var validEvent = eventValidation.validate(
                creatorId,
                request.schedule(),
                request.durationInSeconds(),
                request.latitude(),
                request.longitude()
        );
        if (validEvent.isFailure()) {
            return Result.failure(validEvent.getError());
        }
        var imageFileName = UUID.randomUUID().toString();
        var maybeEvent = Event.create(
                request.title(),
                request.description(),
                request.schedule(),
                request.durationInSeconds(),
                request.locationName(),
                request.locationCity(),
                request.locationCountry(),
                request.latitude(),
                request.longitude(),
                request.categories(),
                request.isPublic(),
                request.enableComments(),
                Modality.fromString(request.modality()),
                imageFileName,
                request.staffs(),
                creatorId,
                request.maxAttendees()
        );
        if (maybeEvent.isFailure()) {
            return Result.failure(maybeEvent.getError());
        }
        if (imageStream != null) {
            var imageStored = fileStorage.storeImage(imageFileName, imageStream);
            if (imageStored == null) {
                return Result.failure(EventAppError.IMAGE_STORAGE_ERROR);
            }
        }
        var createdEvent = maybeEvent.getValue();
        eventRepository.save(createdEvent);
        return Result.success(createdEvent);
    }
}
