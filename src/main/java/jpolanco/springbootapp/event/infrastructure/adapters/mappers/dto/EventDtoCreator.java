package jpolanco.springbootapp.event.infrastructure.adapters.mappers.dto;

import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response.EventResponseDto;
import jpolanco.springbootapp.shared.application.Dto;
import jpolanco.springbootapp.shared.application.DtoCreator;
import org.springframework.stereotype.Component;

@Component
public class EventDtoCreator implements DtoCreator<Event> {

    @Override
    public Dto create(Event payload) {
        if (payload == null) {
            return null;
        }
        return new EventResponseDto(
                payload.getEventId(),
                payload.getTitle(),
                payload.getDescription(),
                payload.getSchedule().toString(),
                payload.getDurationInSeconds(),
                payload.getCreatorId()
        );
    }
}
