package jpolanco.springbootapp.event.application.ports.input;

public record StaffHolder(
        String staffId,
        String role,
        boolean isAssistanceClerk
) {
}
