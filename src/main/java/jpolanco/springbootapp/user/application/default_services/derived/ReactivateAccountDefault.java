package jpolanco.springbootapp.user.application.default_services.derived;

import jpolanco.springbootapp.shared.application.AppError;
import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.use_case.base.ReactivateUserUC;
import jpolanco.springbootapp.user.application.use_case.derived.ReactivateProfileUC;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ReactivateProfile implements ReactivateProfileUC {
    private final UserQueryRepository queryRepository;
    private final ReactivateUserUC reactivateUserUC;

    @Override
    public Result<List<EventNotification>> reactivate(UUID userId) {
        var maybeUser = queryRepository.findByUuid(userId);
        if (maybeUser.isEmpty()) return Result.failure(AppError.idNotFound(userId, "User"));

        var user = maybeUser.get();
        if (user.isSuspended()) return Result.failure(AppError.INVALID_OPERATION
                    .withMessage("is suspended and cannot be reactivated"));

        return reactivateUserUC.reactivate(user);
    }
}
