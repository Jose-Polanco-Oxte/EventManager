package jpolanco.springbootapp.user.infrastructure.services.implementations;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.infrastructure.dto.CursorPageResponseDto;
import jpolanco.springbootapp.shared.infrastructure.dto.PageResponseDto;
import jpolanco.springbootapp.shared.infrastructure.mappers.CursorPageCreator;
import jpolanco.springbootapp.shared.infrastructure.mappers.PageCreator;
import jpolanco.springbootapp.user.application.uc.*;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserResponseDto;
import jpolanco.springbootapp.user.infrastructure.adapters.mappers.dto.UserDtoCreator;
import jpolanco.springbootapp.user.infrastructure.services.interfaces.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserQueryServiceImpl implements UserQueryService {
    private final GetUserByIdUC getUserByIdUC;
    private final GetUserByEmailUC getUserByEmailUC;
    private final GetAllUsersUC getAllUsersUC;
    private final PageGetUsersUC PageGetUsersUC;
    private final CursorGetUsersUC CursorGetUsersUC;
    private final UserDtoCreator userDtoCreator;
    private final CursorPageCreator<User, UserResponseDto, String> cursorPageCreator;
    private final PageCreator<User, UserResponseDto> pageCreator;

    @Override
    public Result<UserResponseDto> getUserById(String userId) {
        var maybeUser = getUserByIdUC.get(userId);
        if (maybeUser.isFailure()) {
            return Result.failure(maybeUser.getError());
        }
        var user = maybeUser.getValue();
        return Result.success(userDtoCreator.create(user));
    }

    @Override
    public Result<UserResponseDto> getUserByEmail(String email) {
        var maybeUser = getUserByEmailUC.get(email);
        if (maybeUser.isFailure()) {
            return Result.failure(maybeUser.getError());
        }
        var user = maybeUser.getValue();
        return Result.success(userDtoCreator.create(user));
    }

    @Override
    public PageResponseDto<UserResponseDto> getUsers(int page, int size, String sortBy, String sortOrder) {
        return pageCreator.create(PageGetUsersUC.get(page, size, sortBy, sortOrder), new UserDtoCreator());
    }

    @Override
    public CursorPageResponseDto<UserResponseDto, String> getUsers(String cursor, int size, String sortBy, String sortOrder) {
        return cursorPageCreator
                .create(CursorGetUsersUC.get(cursor, size, sortBy, sortOrder), new UserDtoCreator());
    }

    @Override
    public List<UserResponseDto> getAll() {
        var maybeUsers = getAllUsersUC.get();
        if (maybeUsers.isEmpty()) {
            return List.of();
        }
        return maybeUsers.stream()
                .map(userDtoCreator::create)
                .toList();
    }
}
