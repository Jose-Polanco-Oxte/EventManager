package jpolanco.springbootapp.shared.infrastructure.mappers;

import jpolanco.springbootapp.shared.infrastructure.dto.Dto;
import jpolanco.springbootapp.shared.infrastructure.dto.DtoCreator;
import jpolanco.springbootapp.shared.application.utils.PageResult;
import jpolanco.springbootapp.shared.infrastructure.dto.SlicePageResponseDto;
import org.springframework.stereotype.Component;

@Component
public class SlicePageCreator<T, D extends Dto> {

    public SlicePageResponseDto<D> create(PageResult<T> result, DtoCreator<T, D> dtoCreator) {
        return new SlicePageResponseDto<>(
                result.getItems().stream().map(dtoCreator::create).toList(),
                result.isHasNext()
        );
    }
}
