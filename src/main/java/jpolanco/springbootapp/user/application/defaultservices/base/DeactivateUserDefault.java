package jpolanco.springbootapp.user.application.defaultservices.base;

import jpolanco.springbootapp.shared.application.AppError;
import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.utils.results.Result;
import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.usecase.base.DeactivateUser;
import jpolanco.springbootapp.user.domain.model.valueobjects.User;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class DeactivateUserDefault implements DeactivateUser {
    private final UserQueryRepository queryRepository;
    private final UserCommandRepository commandRepository;

    @Override
    public Result<List<EventNotification>> deactivate(User user, String reason) {
        if (user.isInactive()) {
            return Result.failure(AppError.INVALID_OPERATION
                    .withMessage("is already inactive and cannot be deactivated"));
        }
        user.deactivate(reason);
        var savedUser = commandRepository.save(user);
        return Result.success(savedUser.pullEvents());
    }

    @Override
    public Result<List<EventNotification>> deactivateById(UUID userId, String reason) {
        var maybeUser = queryRepository.findByUuid(userId);
        if (maybeUser.isEmpty()) return Result.failure(AppError.idNotFound(userId, "User"));

        var user = maybeUser.get();
        return deactivate(user, reason);
    }
}
