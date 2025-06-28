package jpolanco.springbootapp.shared.infrastructure.mappers;

import jpolanco.springbootapp.shared.infrastructure.dto.interfaces.Dto;
import jpolanco.springbootapp.shared.infrastructure.dto.interfaces.DtoCreator;
import jpolanco.springbootapp.shared.application.PageResult;
import jpolanco.springbootapp.shared.infrastructure.dto.response.SlicePageResponse;
import jpolanco.springbootapp.user.infrastructure.adapters.mappers.dto.ComposedDtoCreator;
import org.springframework.stereotype.Component;

@Component
public class SlicePageCreator<Entity, D extends Dto>
        implements ComposedDtoCreator<Entity,
        PageResult<Entity>,
        D, DtoCreator<Entity, D>,
        SlicePageResponse<D>> {

    @Override
    public SlicePageResponse<D> create(PageResult<Entity> entityPageResult, DtoCreator<Entity, D> creator) {
        return new SlicePageResponse<>(
                entityPageResult.items().stream()
                        .map(creator::create)
                        .toList(),
                entityPageResult.hasNext()
        );
    }
}
