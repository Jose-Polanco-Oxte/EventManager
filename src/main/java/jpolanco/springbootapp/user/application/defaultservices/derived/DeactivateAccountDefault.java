package jpolanco.springbootapp.user.application.defaultservices.derived;

import jpolanco.springbootapp.shared.application.AppError;
import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.utils.results.Result;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.usecase.base.DeactivateUser;
import jpolanco.springbootapp.user.application.usecase.derived.DeactivateAccount;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class DeactivateAccountDefault implements DeactivateAccount {
    private final UserQueryRepository queryRepository;
    private final DeactivateUser deactivateUser;

    @Override
    public Result<List<EventNotification>> deactivate(UUID userId, String reason) {
        var maybeUser = queryRepository.findByUuid(userId);
        if (maybeUser.isEmpty()) return Result.failure(AppError.idNotFound(userId, "User"));

        var user = maybeUser.get();
        if (user.isSuspended()) return Result.failure(AppError.INVALID_OPERATION
                .withMessage("userId is suspended and cannot be deactivated"));

        return deactivateUser.deactivate(user, reason);
    }
}
