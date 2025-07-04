package jpolanco.springbootapp.user.application.services.derived;

import jpolanco.springbootapp.shared.application.AppError;
import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.uc.base.DeactivateUserUC;
import jpolanco.springbootapp.user.application.uc.derived.DeactivateProfileUC;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class DeactivateProfile implements DeactivateProfileUC {
    private final UserQueryRepository queryRepository;
    private final DeactivateUserUC deactivateUserUC;

    @Override
    public Result<List<EventNotification>> deactivate(UUID userId, String reason) {
        var maybeUser = queryRepository.findByUuid(userId);
        if (maybeUser.isEmpty()) return Result.failure(AppError.idNotFound(userId, "User"));

        var user = maybeUser.get();
        if (user.isSuspended()) return Result.failure(AppError.INVALID_OPERATION
                .withMessage("userId is suspended and cannot be deactivated"));

        return deactivateUserUC.deactivate(user, reason);
    }
}
