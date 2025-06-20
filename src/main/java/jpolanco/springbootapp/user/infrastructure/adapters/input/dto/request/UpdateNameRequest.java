package jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request;

public record UpdateNameRequest(
        String firstName,
        String lastName
){}