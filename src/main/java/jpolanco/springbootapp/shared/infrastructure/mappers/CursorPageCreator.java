package jpolanco.springbootapp.shared.infrastructure.mappers;

import jpolanco.springbootapp.shared.utils.CursorPageResult;
import jpolanco.springbootapp.shared.infrastructure.dto.Dto;
import jpolanco.springbootapp.shared.infrastructure.dto.DtoCreator;
import jpolanco.springbootapp.shared.infrastructure.dto.CursorPageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CursorPageCreator<Entity, D extends Dto, ID> /* implements DtoCreator<CursorPageResult<Entity, ID>, CursorPageResponseDto<D, ID>> */{
//    private final DtoCreator<Entity, D> dtoCreator;
//
//    @Override
//    public CursorPageResponseDto<D, ID> create(CursorPageResult<Entity, ID> payload) {
//        return new CursorPageResponseDto<>(
//                payload.items().stream().map(dtoCreator::create).toList(),
//                payload.lastItemId(),
//                payload.hasNextPage()
//        );
//    }

    public CursorPageResponseDto<D, ID> create(CursorPageResult<Entity, ID> payload, DtoCreator<Entity, D> dtoCreator) {
        return new CursorPageResponseDto<>(
                payload.items().stream().map(dtoCreator::create).toList(),
                payload.lastItemId(),
                payload.hasNextPage()
        );
    }
}
