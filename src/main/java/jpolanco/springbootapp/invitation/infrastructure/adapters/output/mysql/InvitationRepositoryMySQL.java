package jpolanco.springbootapp.invitation.infrastructure.adapters.output.mysql;

import jpolanco.springbootapp.invitation.application.ports.output.InvitationRepository;
import jpolanco.springbootapp.invitation.domain.model.Invitation;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class InvitationRepositoryMySQL implements InvitationRepository {
    @Override
    public int countInvitationsAcceptedByEventId(String eventId) {
        // Value is not used in the current implementation, but it can be used to count accepted invitations for an event.
        return Integer.MAX_VALUE;
    }

    @Override
    public Invitation save(Invitation entity) {
        return null;
    }

    @Override
    public Optional<Invitation> findById(String s) {
        return Optional.empty();
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public Invitation update(Invitation entity) {
        return null;
    }
}
