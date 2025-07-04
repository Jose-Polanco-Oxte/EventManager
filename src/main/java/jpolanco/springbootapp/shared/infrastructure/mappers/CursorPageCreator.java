package jpolanco.springbootapp.shared.infrastructure.mappers;

import jpolanco.springbootapp.shared.application.pagination.CursorPageResult;
import jpolanco.springbootapp.shared.infrastructure.dto.interfaces.Dto;
import jpolanco.springbootapp.shared.infrastructure.dto.interfaces.DtoCreator;
import jpolanco.springbootapp.shared.infrastructure.dto.response.CursorPageResponse;
import jpolanco.springbootapp.user.infrastructure.adapters.mappers.dto.ComposedDtoCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CursorPageCreator<Entity, D extends Dto, ID>
        implements ComposedDtoCreator<Entity,
        CursorPageResult<Entity, ID>,
        D, DtoCreator<Entity, D>,
        CursorPageResponse<D, ID>> {

    @Override
    public CursorPageResponse<D, ID> create(CursorPageResult<Entity, ID> result, DtoCreator<Entity, D> creator) {
        return new CursorPageResponse<>(
                result.items().stream()
                        .map(creator::create)
                        .toList(),
                result.lastItemId(),
                result.hasNextPage()
        );
    }

    public static <Entity, D extends Dto, ID> CursorPageCreator<Entity, D, ID> getInstance() {
        return new CursorPageCreator<>();
    }
}
