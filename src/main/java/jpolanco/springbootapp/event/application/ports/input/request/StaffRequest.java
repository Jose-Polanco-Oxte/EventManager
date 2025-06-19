package jpolanco.springbootapp.event.application.ports.input.request;

public record StaffRequest(
        String staffId,
        String role,
        boolean isAssistanceClerk
) {
}
