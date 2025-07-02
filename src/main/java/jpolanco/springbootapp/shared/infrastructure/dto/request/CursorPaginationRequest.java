package jpolanco.springbootapp.shared.infrastructure.dto.request;

public record CursorPaginationRequest<C>(
    C cursor,
    int size,
    String sortBy,
    String orderBy
) implements PaginationRequest {
}