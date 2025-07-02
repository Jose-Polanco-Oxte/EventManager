package jpolanco.springbootapp.shared.infrastructure.dto.request;

public record PagePaginationRequest(
    int page,
    int size,
    String sortBy,
    String orderBy
) implements PaginationRequest {
}
