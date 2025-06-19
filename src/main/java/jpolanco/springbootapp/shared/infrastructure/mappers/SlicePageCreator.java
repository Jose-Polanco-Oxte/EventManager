package jpolanco.springbootapp.shared.infrastructure.mappers;

import jpolanco.springbootapp.shared.infrastructure.dto.DtoCreator;
import jpolanco.springbootapp.shared.utils.PageResult;
import jpolanco.springbootapp.shared.infrastructure.dto.SlicePageResponseDto;
import org.springframework.stereotype.Component;

@Component
public class SlicePageCreator<Entity, Dto extends jpolanco.springbootapp.shared.infrastructure.dto.Dto> {

    public SlicePageResponseDto<Dto> create(PageResult<Entity> result, DtoCreator<Entity, Dto> dtoCreator) {
        return new SlicePageResponseDto<>(
                result.getItems().stream().map(dtoCreator::create).toList(),
                result.isHasNext()
        );
    }
}
