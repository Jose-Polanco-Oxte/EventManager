package jpolanco.springbootapp.user.infrastructure.services.implementations;

import jpolanco.springbootapp.shared.infrastructure.dto.request.CursorPaginationRequest;
import jpolanco.springbootapp.shared.infrastructure.dto.request.PagePaginationRequest;
import jpolanco.springbootapp.shared.application.CursorPageResult;
import jpolanco.springbootapp.shared.application.PageResult;
import jpolanco.springbootapp.user.application.uc.unique.GetUserByEmailUC;
import jpolanco.springbootapp.user.application.uc.unique.GetUserByIdUC;
import jpolanco.springbootapp.user.application.uc.unique.GetUsersUC;
import jpolanco.springbootapp.user.domain.model.value_objects.User;
import jpolanco.springbootapp.user.infrastructure.services.interfaces.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {
    private final GetUserByIdUC getUserByIdUC;
    private final GetUserByEmailUC getUserByEmailUC;
    private final GetUsersUC getUsersUC;

    @Override
    public Optional<User> getById(UUID userId) {
        return getUserByIdUC.get(userId);
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return getUserByEmailUC.get(email);
    }

    @Override
    public PageResult<User> getByPages(int page, int size, String sortBy, String orderBy) {
        return getUsersUC.getByPages(new PagePaginationRequest(page, size, sortBy, orderBy));
    }

    @Override
    public CursorPageResult<User, UUID> getByCursor(UUID cursor, int size, String sortBy, String orderBy) {
        return getUsersUC.getByCursor(new CursorPaginationRequest<>(cursor, size, sortBy, orderBy));
    }
}
