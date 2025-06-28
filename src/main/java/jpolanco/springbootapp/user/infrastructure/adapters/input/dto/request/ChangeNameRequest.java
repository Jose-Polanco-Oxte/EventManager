package jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request;

public record ChangeNameRequest(
        String firstName,
        String lastName
){}