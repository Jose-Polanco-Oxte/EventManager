package jpolanco.springbootapp.user.infrastructure.services.implementations;

import jpolanco.springbootapp.shared.infrastructure.dto.request.CursorPaginationRequest;
import jpolanco.springbootapp.shared.infrastructure.dto.request.PagePaginationRequest;
import jpolanco.springbootapp.shared.application.pagination.CursorPageResult;
import jpolanco.springbootapp.shared.application.pagination.PageResult;
import jpolanco.springbootapp.user.application.usecase.unique.GetUserByEmail;
import jpolanco.springbootapp.user.application.usecase.unique.GetUserByUUID;
import jpolanco.springbootapp.user.application.usecase.unique.GetUsers;
import jpolanco.springbootapp.user.domain.model.valueobjects.User;
import jpolanco.springbootapp.user.infrastructure.services.interfaces.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {
    private final GetUserByUUID getUserByUUID;
    private final GetUserByEmail getUserByEmail;
    private final GetUsers getUsers;

    @Override
    public Optional<User> getById(UUID userId) {
        return getUserByUUID.get(userId);
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return getUserByEmail.get(email);
    }

    @Override
    public PageResult<User> getByPages(int page, int size, String sortBy, String orderBy) {
        return getUsers.getByPages(new PagePaginationRequest(page, size, sortBy, orderBy));
    }

    @Override
    public CursorPageResult<User, UUID> getByCursor(UUID cursor, int size, String sortBy, String orderBy) {
        return getUsers.getByCursor(new CursorPaginationRequest<>(cursor, size, sortBy, orderBy));
    }
}
