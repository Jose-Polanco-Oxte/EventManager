package jpolanco.springbootapp.event.application.ports.input.request;

public record CursorPaginationRequest<C>(
    C cursor,
    int size,
    String sortBy,
    String orderBy
) implements PaginationRequest {
}