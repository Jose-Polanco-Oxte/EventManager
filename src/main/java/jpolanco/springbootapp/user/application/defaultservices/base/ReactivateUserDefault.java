package jpolanco.springbootapp.user.application.defaultservices.base;

import jpolanco.springbootapp.shared.application.AppError;
import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.utils.results.Result;
import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.usecase.base.ReactivateUser;
import jpolanco.springbootapp.user.domain.model.valueobjects.User;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ReactivateUserDefault implements ReactivateUser {
    private final UserQueryRepository queryRepository;
    private final UserCommandRepository commandRepository;

    @Override
    public Result<List<EventNotification>> reactivate(User user) {
        if (user.isActive()) {
            return Result.failure(AppError.INVALID_OPERATION
                    .withMessage("is already active and cannot be reactivated"));
        }
        user.reactivate();
        var savedUser = commandRepository.save(user);
        return Result.success(savedUser.pullEvents());
    }

    @Override
    public Result<List<EventNotification>> reactivateById(UUID userId) {
        var maybeUser = queryRepository.findByUuid(userId);
        if (maybeUser.isEmpty()) return Result.failure(AppError.idNotFound(userId, "User"));

        var user = maybeUser.get();
        return reactivate(user);
    }
}
