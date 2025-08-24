package jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request;

import jakarta.validation.constraints.Min;
import jpolanco.applicationcore.shared.infrastructure.utils.PageableRequest;
import jpolanco.applicationcore.shared.infrastructure.utils.types.Direction;
import jpolanco.applicationcore.shared.infrastructure.utils.types.SortValue;
import jpolanco.applicationcore.user.infrastructure.components.utils.UserSort;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.UUID;

@Validated
public record CursorRequest(Optional<Direction> sortDirection, Optional<UserSort> sortBy,
                            @Min(1) Optional<Integer> size, Optional<UUID> cursor) implements PageableRequest {
    @Override
    public int getPage() {
        return 0; // Cursor-based pagination does not use page numbers
    }

    @Override
    public int getSize() {
        return size.orElse(10); // Default size if not provided
    }

    @Override
    public SortValue getSortBy() {
        return sortBy.orElse(UserSort.UNSORTED);
    }

    @Override
    public Direction getDirection() {
        return sortDirection.orElse(Direction.ASC);
    }
}
