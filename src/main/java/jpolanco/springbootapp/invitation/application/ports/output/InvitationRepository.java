package jpolanco.springbootapp.invitation.application.ports.output;

import jpolanco.springbootapp.invitation.domain.model.Invitation;
import jpolanco.springbootapp.shared.domain.CRUDRepository;

public interface InvitationRepository extends CRUDRepository<Invitation, String> {
    int countInvitationsAcceptedByEventId(String eventId);
}
