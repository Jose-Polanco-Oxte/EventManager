package jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request;

import jakarta.validation.constraints.Min;
import jpolanco.applicationcore.shared.infrastructure.utils.PageableRequest;
import jpolanco.applicationcore.shared.infrastructure.utils.types.Direction;
import jpolanco.applicationcore.user.infrastructure.components.utils.UserSort;

import java.util.Optional;

public record PagesRequest(Optional<Direction> direction, Optional<UserSort> sort, @Min(0) Optional<Integer> page,
                           @Min(1) Optional<Integer> size) implements PageableRequest {
    @Override
    public int getPage() {
        return page.orElse(0);
    }

    @Override
    public int getSize() {
        return size.orElse(10);
    }

    @Override
    public UserSort getSortBy() {
        return sort.orElse(UserSort.UNSORTED);
    }

    @Override
    public Direction getDirection() {
        return direction.orElse(Direction.ASC);
    }
}