package jpolanco.springbootapp.user.application.services.base;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.application.errors.UserAppError;
import jpolanco.springbootapp.user.application.ports.input.QRProvider;
import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.utils.UserUpdater;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.domain.model.value_objects.UserStatus;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.AnyUserUpdateRequest;
import jpolanco.springbootapp.user.application.utils.UserValidation;
import jpolanco.springbootapp.user.application.uc.base.UpdateUserUC;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateUser implements UpdateUserUC {
    private final UserQueryRepository queryRepository;
    private final UserCommandRepository commandRepository;
    private final PasswordEncoder passwordEncoder;
    private final QRProvider qrProvider;
    private final UserValidation userValidation;

    @Override
    public Result<User> setChanges(User user, AnyUserUpdateRequest request) {
        var userUpdater = UserUpdater.updater(user, passwordEncoder, qrProvider, userValidation);
        var userUpdated = userUpdater
                .firstName(request.firstName())
                .lastName(request.lastName())
                .roles(request.roles().add(), request.roles().remove())
                .status(UserStatus.fromString(request.status()))
                .email(request.email())
                .password(request.password())
                .update();
        if (userUpdated.isFailure()) {
            return Result.failure(userUpdated.getError());
        }
        commandRepository.save(userUpdated.getValue());
        return Result.success(userUpdated.getValue());
    }

    @Override
    public Result<User> setChanges(String userId, AnyUserUpdateRequest request) {
        var maybeUser = queryRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            return Result.failure(UserAppError.USER_NOT_FOUND);
        }
        var user = maybeUser.get();
        return setChanges(user, request);
    }
}