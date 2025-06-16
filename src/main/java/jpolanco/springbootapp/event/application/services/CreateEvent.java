package jpolanco.springbootapp.event.application.services;

import jpolanco.springbootapp.event.application.errors.EventAppError;
import jpolanco.springbootapp.event.application.ports.input.FileStorageProvider;
import jpolanco.springbootapp.event.application.ports.output.EventRepository;
import jpolanco.springbootapp.event.application.uc.CreateEventUC;
import jpolanco.springbootapp.event.application.utils.EventValidation;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.EventCreationDto;
import jpolanco.springbootapp.shared.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CreateEvent implements CreateEventUC {

    private final EventRepository eventRepository;
    private final EventValidation eventValidation;
    private final FileStorageProvider fileStorage;

    @Override
     public Result<Event> create(EventCreationDto event, String creatorId, InputStream imageStream) {
        var valid = eventValidation.validate(
                creatorId,
                Instant.parse(event.schedule()),
                event.durationInSeconds(),
                event.latitude(),
                event.longitude());
        if (valid.isFailure()) {
            return Result.failure(valid.getError());
        }
        var imageFileName = UUID.randomUUID().toString();
        // Create new event using the provided details
        var maybeEvent = Event.create(
                event.title(),
                event.description(),
                event.schedule(),
                event.durationInSeconds(),
                event.locationName(),
                event.locationCity(),
                event.locationCountry(),
                event.latitude(),
                event.longitude(),
                event.categories(),
                event.isPublic(),
                event.enableComments(),
                event.modality(),
                imageFileName,
                event.staffs(),
                creatorId,
                event.maxAttendees()
        );
        // Check if event creation was successful
        if (maybeEvent.isFailure()) {
            return Result.failure(maybeEvent.getError());
        }
        // Store the event image if provided
        if (imageStream != null) {
            var imageStored = fileStorage.storeImage(imageFileName, imageStream);
            if (imageStored == null) {
                return Result.failure(EventAppError.IMAGE_STORAGE_ERROR);
            }
        }
        // Save the event to the repository
        var createdEvent = maybeEvent.getValue();
        eventRepository.save(createdEvent);
        // Return the created event wrapped in a Result object
        return Result.success(createdEvent);
    }
}
