package jpolanco.springbootapp.event.application.services;

import jpolanco.springbootapp.event.application.uc.UpdateMyEventUC;
import jpolanco.springbootapp.event.application.utils.EventUpdater;
import jpolanco.springbootapp.event.application.utils.EventValidation;
import jpolanco.springbootapp.event.infrastructure.errors.EventIntegrity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service("My")
public class UpdateMyEvent implements UpdateMyEventUC {

    private final EventValidation eventValidation;

    @Override
    public EventUpdater setChanges(String eventId, String creatorId) {
        var result = eventValidation.validatePropietary(eventId, creatorId);
        if (result.isFailure()) {
            throw new EventIntegrity(result.getMessage());
        }
        var event = result.getValue();
        return new EventUpdater(event, eventValidation);
    }
}
