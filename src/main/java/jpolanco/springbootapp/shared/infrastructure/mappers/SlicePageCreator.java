package jpolanco.springbootapp.shared.infrastructure.mappers;

import jpolanco.springbootapp.shared.application.Dto;
import jpolanco.springbootapp.shared.application.DtoCreator;
import jpolanco.springbootapp.shared.application.PageResult;
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
