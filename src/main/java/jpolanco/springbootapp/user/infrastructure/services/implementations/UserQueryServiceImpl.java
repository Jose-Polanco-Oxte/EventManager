package jpolanco.springbootapp.user.infrastructure.services.implementations;

import jpolanco.springbootapp.event.application.ports.input.request.CursorPaginationRequest;
import jpolanco.springbootapp.event.application.ports.input.request.PagePaginationRequest;
import jpolanco.springbootapp.shared.infrastructure.dto.CursorPageResponseDto;
import jpolanco.springbootapp.shared.infrastructure.dto.PageResponseDto;
import jpolanco.springbootapp.shared.infrastructure.mappers.CursorPageCreator;
import jpolanco.springbootapp.shared.infrastructure.mappers.PageCreator;
import jpolanco.springbootapp.user.application.uc.unique.GetUserByEmailUC;
import jpolanco.springbootapp.user.application.uc.unique.GetUserByIdUC;
import jpolanco.springbootapp.user.application.uc.unique.GetUsersUC;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserResponse;
import jpolanco.springbootapp.user.infrastructure.adapters.mappers.dto.UserDtoCreator;
import jpolanco.springbootapp.user.infrastructure.services.interfaces.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/*
 * WARNING: This code is under refactoring and now not be functional.
 */

@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {
    private final GetUserByIdUC getUserByIdUC;
    private final GetUserByEmailUC getUserByEmailUC;
    private final GetUsersUC getUsersUC;
    private final UserDtoCreator userDtoCreator;
    private final CursorPageCreator<User, UserResponse, String> cursorPageCreator;
    private final PageCreator<User, UserResponse> pageCreator;

    @Override
    public Optional<UserResponse> getById(String userId) {
        return getUserByIdUC.get(userId)
                .map(userDtoCreator::create);
    }

    @Override
    public Optional<UserResponse> getByEmail(String email) {
        return getUserByEmailUC.get(email)
                .map(userDtoCreator::create);
    }

    @Override
    public PageResponseDto<UserResponse> get(int page, int size, String sortBy, String orderBy) {
        return pageCreator
                .create(getUsersUC
                        .getByPages(new PagePaginationRequest(page, size, sortBy, orderBy)), new UserDtoCreator());
    }

    @Override
    public CursorPageResponseDto<UserResponse, String> get(String cursor, int size, String sortBy, String orderBy) {
        return cursorPageCreator
                .create(getUsersUC
                        .getByCursor(new CursorPaginationRequest<>(cursor, size, sortBy, orderBy)), new UserDtoCreator());
    }
}
