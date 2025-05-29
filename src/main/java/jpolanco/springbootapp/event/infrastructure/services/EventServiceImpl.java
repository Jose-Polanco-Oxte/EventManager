package jpolanco.springbootapp.event.infrastructure.services;

import jpolanco.springbootapp.event.application.uc.CreateEventUC;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.EventCreationDto;
import jpolanco.springbootapp.event.infrastructure.services.interfaces.EventService;
import jpolanco.springbootapp.shared.application.Dto;
import jpolanco.springbootapp.shared.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {

    private final CreateEventUC createEventUC;

    @Override
    public Result<Dto> createEvent(EventCreationDto eventDto) {
        var createdEvent = createEventUC.create(eventDto, null, null);
        return null;
    }
}
