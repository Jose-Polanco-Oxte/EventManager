package jpolanco.springbootapp.user.infrastructure.services;

import jpolanco.springbootapp.shared.application.Dto;
import jpolanco.springbootapp.user.application.ports.input.AuxTokenManager;
import jpolanco.springbootapp.user.application.uc.*;
import jpolanco.springbootapp.user.application.utils.UserValidation;
import jpolanco.springbootapp.user.infrastructure.adapters.mappers.dto.SimpleResponseCreator;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.infrastructure.adapters.mappers.dto.UserDtoCreator;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.AllUserUpdateRequest;
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

    @Override
    public Result<Dto> getUserById(String userId) {
        var maybeUser = getUserByIdUC.get(userId);
        if (maybeUser.isFailure()) {
            return Result.failure(maybeUser.getError());
        }
        var user = maybeUser.getValue();
        return Result.success(dtoCreator.create(user));
    }

    @Override
    public Result<Dto> getUserByEmail(String email) {
        var maybeUser = getUserByEmailUC.get(email);
        if (maybeUser.isFailure()) {
            return Result.failure(maybeUser.getError());
        }
        var user = maybeUser.getValue();
        return Result.success(dtoCreator.create(user));
    }

    @Override
    public Result<List<Dto>> getAll() {
        var maybeUsers = getAllUsersUC.getAll();
        if (maybeUsers.isFailure()) {
            return Result.failure(maybeUsers.getError());
        }
        var users = maybeUsers.getValue();
        return Result.success(users.stream().map(dtoCreator::create).toList());
    }

    @Override
    public Result<List<Dto>> searchUsers(String searchTerm, int page, int size) {
        return null;
    }

    @Override
    @Transactional
    public Result<Dto> updateUser(AllUserUpdateRequest dto, String userId) {
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
    public Result<Dto> deleteUser(String userId) {
        var result = deleteUserByIdUC.delete(userId);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        return Result.success(simpleResponseDto.create("User deleted successfully"));
    }
}
