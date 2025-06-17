package jpolanco.springbootapp.shared.infrastructure.mappers;
import jpolanco.springbootapp.shared.infrastructure.dto.Dto;
import jpolanco.springbootapp.shared.infrastructure.dto.DtoCreator;
import jpolanco.springbootapp.shared.application.utils.PageResult;
import jpolanco.springbootapp.shared.infrastructure.dto.PageResponseDto;
import org.springframework.stereotype.Component;

@Component
public class PageCreator<T, D extends Dto> {

    public PageResponseDto<D> create(PageResult<T> result, DtoCreator<T, D> dtoCreator) {
        return new PageResponseDto<>(
                result.getItems().stream().map(dtoCreator::create).toList(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.isHasNext()
        );
    }
}
