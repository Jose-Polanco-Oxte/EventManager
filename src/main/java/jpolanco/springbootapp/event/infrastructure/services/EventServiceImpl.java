package jpolanco.springbootapp.event.infrastructure.services;

import jpolanco.springbootapp.event.application.uc.CreateEventUC;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.EventCreationDto;
import jpolanco.springbootapp.event.infrastructure.adapters.mappers.dto.EventDtoCreator;
import jpolanco.springbootapp.event.infrastructure.services.interfaces.EventService;
import jpolanco.springbootapp.shared.application.Dto;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.infrastructure.publisher.DomainEventsPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {

    private final CreateEventUC createEventUC;
    private final EventDtoCreator eventDtoCreator;
    private final DomainEventsPublisher publisher;

    @Transactional
    @Override
    public Result<Dto> createEvent(EventCreationDto eventDto, String creatorId, String imageFileName) {
        var createdEvent = createEventUC.create(eventDto, creatorId, imageFileName);
        if (createdEvent.isFailure()) {
            return Result.failure(createdEvent.getError());
        }
        var event = createdEvent.getValue();
        event.pullEvents().forEach(publisher::publish);
        return Result.success(eventDtoCreator.create(createdEvent.getValue()));
    }
}
