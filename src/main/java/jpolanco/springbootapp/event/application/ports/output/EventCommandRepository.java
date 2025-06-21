package jpolanco.springbootapp.event.application.ports.output;

import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.application.adapters.output.CUDRepository;

public interface EventCommandRepository extends CUDRepository<Event, String> {
}
