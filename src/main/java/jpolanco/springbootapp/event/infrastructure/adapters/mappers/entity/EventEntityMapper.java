package jpolanco.springbootapp.event.infrastructure.adapters.mappers.entity;

import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.event.infrastructure.adapters.output.persistence.EventEntity;
import jpolanco.springbootapp.shared.infrastructure.mappers.EntityMapper;

public interface EventEntityMapper extends EntityMapper<EventEntity, Event> {
}
