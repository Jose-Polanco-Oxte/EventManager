package jpolanco.springbootapp.event.infrastructure.services.interfaces;

import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.EventCreationDto;
import jpolanco.springbootapp.shared.application.Dto;
import jpolanco.springbootapp.shared.domain.Result;

public interface EventService {
    Result<Dto> createEvent(EventCreationDto eventDto);
}
