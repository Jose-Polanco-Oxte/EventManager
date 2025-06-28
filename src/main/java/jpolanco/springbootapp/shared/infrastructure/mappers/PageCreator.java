package jpolanco.springbootapp.shared.infrastructure.mappers;
import jpolanco.springbootapp.shared.infrastructure.dto.interfaces.Dto;
import jpolanco.springbootapp.shared.infrastructure.dto.interfaces.DtoCreator;
import jpolanco.springbootapp.shared.application.PageResult;
import jpolanco.springbootapp.shared.infrastructure.dto.response.PageResponse;
import jpolanco.springbootapp.user.infrastructure.adapters.mappers.dto.ComposedDtoCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PageCreator<Entity, D extends Dto>
        implements ComposedDtoCreator<Entity,
        PageResult<Entity>,
        D, DtoCreator<Entity, D>,
        PageResponse<D>> {
    @Override
    public PageResponse<D> create(PageResult<Entity> entityPageResult, DtoCreator<Entity, D> creator) {
        return new PageResponse<>(
                entityPageResult.items().stream()
                        .map(creator::create)
                        .toList(),
                entityPageResult.totalElements(),
                entityPageResult.totalPages(),
                entityPageResult.hasNext()
        );
    }

    public static <Entity, D extends Dto> PageCreator<Entity, D> getInstance() {
        return new PageCreator<>();
    }
}
