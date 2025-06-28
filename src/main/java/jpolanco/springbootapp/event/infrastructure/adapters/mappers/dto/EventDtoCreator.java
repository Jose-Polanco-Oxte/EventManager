package jpolanco.springbootapp.event.infrastructure.adapters.mappers.dto;

import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response.EventResponse;
import jpolanco.springbootapp.shared.infrastructure.dto.interfaces.DtoCreator;
import org.springframework.stereotype.Component;

@Component
public class EventDtoCreator implements DtoCreator<Event, EventResponse> {

    @Override
    public EventResponse create(Event payload) {
        if (payload == null) {
            return null;
        }
        return new EventResponse(
                payload.getEventId(),
                payload.getTitle(),
                payload.getDescription(),
                payload.getSchedule().toString(),
                payload.getDurationInSeconds(),
                payload.getCreatorId()
        );
    }
}
