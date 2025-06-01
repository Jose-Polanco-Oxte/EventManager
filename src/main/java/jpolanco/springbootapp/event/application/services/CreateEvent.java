package jpolanco.springbootapp.event.application.services;

import jpolanco.springbootapp.event.application.ports.input.ImageStorageProvider;
import jpolanco.springbootapp.event.application.ports.output.EventRepository;
import jpolanco.springbootapp.event.application.uc.CreateEventUC;
import jpolanco.springbootapp.event.application.utils.EventValidation;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.EventCreationDto;
import jpolanco.springbootapp.shared.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@RequiredArgsConstructor
@Service
public class CreateEvent implements CreateEventUC {

    private final EventRepository eventRepository;
    private final EventValidation eventValidation;
    private final ImageStorageProvider imageStorageProvider;

    @Override
     public Result<Event> create(EventCreationDto event, String creatorId, String imageFileName) {
        var valid = eventValidation.validate(
                Instant.parse(event.schedule()),
                event.durationInSeconds(),
                event.latitude(),
                event.longitude());
        if (valid.isFailure()) {
            return Result.failure(valid.getError());
        }
        // Create new event using the provided details
        var maybeEvent = Event.create(
                event.title(),
                event.description(),
                event.schedule(),
                event.durationInSeconds(),
                event.latitude(),
                event.longitude(),
                event.categories(),
                event.isPublic(),
                event.enableComments(),
                event.modality(),
                imageFileName,
                event.staffs(),
                creatorId
        );
        // Check if event creation was successful
        if (maybeEvent.isFailure()) {
            return Result.failure(maybeEvent.getError());
        }
        // Save the event to the repository
        var createdEvent = maybeEvent.getValue();
        eventRepository.save(createdEvent);
        // Return the created event wrapped in a Result object
        return Result.success(createdEvent);
    }
}
