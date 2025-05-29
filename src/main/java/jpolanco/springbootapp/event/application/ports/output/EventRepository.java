package jpolanco.springbootapp.event.application.ports.output;

import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.domain.CRUDRepository;

public interface EventRepository extends CRUDRepository<Event, String> {
}
