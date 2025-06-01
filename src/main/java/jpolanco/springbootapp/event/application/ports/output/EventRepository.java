package jpolanco.springbootapp.event.application.ports.output;

import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.domain.CRUDRepository;

import java.time.Instant;

public interface EventRepository extends CRUDRepository<Event, String> {

    boolean sameScheduleExists(Instant date, long duration);

    boolean sameLocationExists(double latitude, double longitude);
}
