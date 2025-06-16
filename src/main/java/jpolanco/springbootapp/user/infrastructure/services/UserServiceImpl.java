package jpolanco.springbootapp.user.infrastructure.services;

import jpolanco.springbootapp.shared.infrastructure.SimpleResponseDto;
import jpolanco.springbootapp.shared.infrastructure.dto.CursorPageResponseDto;
import jpolanco.springbootapp.shared.infrastructure.dto.PageResponseDto;
import jpolanco.springbootapp.shared.infrastructure.mappers.CursorPageCreator;
import jpolanco.springbootapp.shared.infrastructure.mappers.PageCreator;
import jpolanco.springbootapp.user.application.ports.input.AuxTokenManager;
import jpolanco.springbootapp.user.application.uc.*;
import jpolanco.springbootapp.user.application.utils.UserValidation;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserResponseDto;
import jpolanco.springbootapp.user.infrastructure.adapters.mappers.dto.SimpleResponseCreator;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.infrastructure.adapters.mappers.dto.UserDtoCreator;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.AnyUserUpdateRequest;
import jpolanco.springbootapp.user.infrastructure.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final SimpleResponseCreator simpleResponseDto;
    private final UserDtoCreator dtoCreator;
    private final GetUserByIdUC getUserByIdUC;
    private final GetUserByEmailUC getUserByEmailUC;
    private final GetAllUsersUC getAllUsersUC;
    private final UpdateUserUC updateUser;
    private final DeleteUserByIdUC deleteUserByIdUC;
    private final AuxTokenManager auxTokenManager;
    private final UserValidation userValidation;
    private final PGetUsersUC PGetUsersUC;
    private final CGetUsersUC CGetUsersUC;
    private final CursorPageCreator<User, UserResponseDto, String> cursorPageCreator;
    private final PageCreator<User, UserResponseDto> pageCreator;

    @Override
    public Result<UserResponseDto> getUserById(String userId) {
        var maybeUser = getUserByIdUC.get(userId);
        if (maybeUser.isFailure()) {
            return Result.failure(maybeUser.getError());
        }
        var user = maybeUser.getValue();
        return Result.success(dtoCreator.create(user));
    }

    @Override
    public Result<UserResponseDto> getUserByEmail(String email) {
        var maybeUser = getUserByEmailUC.get(email);
        if (maybeUser.isFailure()) {
            return Result.failure(maybeUser.getError());
        }
        var user = maybeUser.getValue();
        return Result.success(dtoCreator.create(user));
    }

    @Override
    public PageResponseDto<UserResponseDto> getUsers(int page, int size, String sortBy, String sortOrder) {
        return pageCreator.create(PGetUsersUC.getUsers(page, size, sortBy, sortOrder), new UserDtoCreator());
    }

    @Override
    public CursorPageResponseDto<UserResponseDto, String> getUsers(String cursor, int size, String sortBy, String sortOrder) {
        return cursorPageCreator.create(CGetUsersUC.getUsers(cursor, size, sortBy, sortOrder), new UserDtoCreator());
    }

    @Override
    public List<UserResponseDto> getAll() {
        var maybeUsers = getAllUsersUC.getAll();
        if (maybeUsers.isEmpty()) {
            return List.of();
        }
        return maybeUsers.stream()
                .map(dtoCreator::create)
                .toList();
    }

    @Override
    @Transactional
    public Result<SimpleResponseDto> updateUser(AnyUserUpdateRequest dto, String userId) {
        var valid = userValidation.basicValid(userId);
        if (valid.isFailure()) {
            return Result.failure(valid.getError());
        }
        var validUser = valid.getValue();
        var updatedUser = updateUser.setChanges(validUser)
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .password(dto.password())
                .status(dto.status())
                .roles(dto.roles())
                .update();
        if (updatedUser.isFailure()) {
            return Result.failure(updatedUser.getError());
        }
        var user = updatedUser.getValue();
        auxTokenManager.revokeAllUserTokens(user.getId());
        return Result.success(simpleResponseDto.create("User updated successfully"));
    }

    @Override
    @Transactional
    public Result<SimpleResponseDto> deleteUser(String userId) {
        var result = deleteUserByIdUC.delete(userId);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        return Result.success(simpleResponseDto.create("User deleted successfully"));
    }
}
