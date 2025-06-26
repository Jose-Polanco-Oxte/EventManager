package jpolanco.springbootapp.shared.infrastructure.mappers;
import jpolanco.springbootapp.shared.infrastructure.dto.Dto;
import jpolanco.springbootapp.shared.infrastructure.dto.DtoCreator;
import jpolanco.springbootapp.shared.utils.PageResult;
import jpolanco.springbootapp.shared.infrastructure.dto.PageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PageCreator<Entity, D extends Dto> /* implements DtoCreator<PageResult<Entity>, PageResponseDto<D>> */ {

//    // This method is not implemented yet, but it should convert a PageResult of Entity to a PageResponseDto of D.
//    @Override
//    public PageResponseDto<D> create(PageResult<Entity> payload) {
////        return new PageResponseDto<>(
////                payload.getItems().stream().map(dtoCreator::create).toList(),
////                payload.getTotalElements(),
////                payload.getTotalPages(),
////                payload.isHasNext()
////        );
//        return null;
//    }

    public PageResponseDto<D> create(PageResult<Entity> payload, DtoCreator<Entity, D> dtoCreator) {
        return new PageResponseDto<>(
                payload.getItems().stream().map(dtoCreator::create).toList(),
                payload.getTotalElements(),
                payload.getTotalPages(),
                payload.isHasNext()
        );
    }
}
