package jpolanco.springbootapp.event.application.ports.input.request;

public record PagePaginationRequest(
    int page,
    int size,
    String sortBy,
    String orderBy
) implements PaginationRequest {
}
