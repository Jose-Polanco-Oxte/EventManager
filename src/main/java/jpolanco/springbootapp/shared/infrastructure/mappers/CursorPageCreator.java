package jpolanco.springbootapp.shared.infrastructure.mappers;

import jpolanco.springbootapp.shared.application.CursorPageResult;
import jpolanco.springbootapp.shared.application.Dto;
import jpolanco.springbootapp.shared.application.DtoCreator;
import jpolanco.springbootapp.shared.infrastructure.dto.CursorPageResponseDto;
import org.springframework.stereotype.Component;

@Component
public class CursorPageCreator<T, D extends Dto, ID> {

    public CursorPageResponseDto<D, ID> create(CursorPageResult<T, ID> result, DtoCreator<T, D> dtoCreator) {
        return new CursorPageResponseDto<>(
                result.items().stream().map(dtoCreator::create).toList(),
                result.lastItemId(),
                result.hasNextPage()
        );
    }
}
