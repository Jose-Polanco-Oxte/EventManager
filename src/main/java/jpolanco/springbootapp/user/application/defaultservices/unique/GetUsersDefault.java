package jpolanco.springbootapp.user.application.defaultservices.unique;

import jpolanco.springbootapp.shared.infrastructure.dto.request.CursorPaginationRequest;
import jpolanco.springbootapp.shared.infrastructure.dto.request.PagePaginationRequest;
import jpolanco.springbootapp.shared.application.pagination.CursorPageResult;
import jpolanco.springbootapp.shared.application.pagination.PageResult;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.usecase.unique.GetUsers;
import jpolanco.springbootapp.user.domain.model.valueobjects.User;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class GetUsersDefault implements GetUsers {
    private final UserQueryRepository queryRepository;

    @Override
    public PageResult<User> getByPages(PagePaginationRequest request) {
        return queryRepository.findAll(
                request.page(),
                request.size(),
                request.sortBy(),
                request.orderBy()
        );
    }

    @Override
    public CursorPageResult<User, UUID> getByCursor(CursorPaginationRequest<UUID> request) {
        return queryRepository.findAll(
                request.cursor(),
                request.size(),
                request.sortBy(),
                request.orderBy()
        );
    }
}
