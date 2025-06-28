package jpolanco.springbootapp.user.infrastructure.services.implementations;

import jpolanco.springbootapp.event.application.ports.input.request.CursorPaginationRequest;
import jpolanco.springbootapp.event.application.ports.input.request.PagePaginationRequest;
import jpolanco.springbootapp.shared.application.CursorPageResult;
import jpolanco.springbootapp.shared.application.PageResult;
import jpolanco.springbootapp.user.application.uc.unique.GetUserByEmailUC;
import jpolanco.springbootapp.user.application.uc.unique.GetUserByIdUC;
import jpolanco.springbootapp.user.application.uc.unique.GetUsersUC;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.infrastructure.services.interfaces.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {
    private final GetUserByIdUC getUserByIdUC;
    private final GetUserByEmailUC getUserByEmailUC;
    private final GetUsersUC getUsersUC;

    @Override
    public Optional<User> getById(String userId) {
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
    public CursorPageResult<User, String> getByCursor(String cursor, int size, String sortBy, String orderBy) {
        return getUsersUC.getByCursor(new CursorPaginationRequest<>(cursor, size, sortBy, orderBy));
    }
}
