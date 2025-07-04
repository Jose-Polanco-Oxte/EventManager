package jpolanco.springbootapp.user.application.defaultservices.derived;

import jpolanco.springbootapp.shared.application.AppError;
import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.utils.results.Result;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.usecase.base.ReactivateUser;
import jpolanco.springbootapp.user.application.usecase.derived.ReactivateAccount;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ReactivateAccountDefault implements ReactivateAccount {
    private final UserQueryRepository queryRepository;
    private final ReactivateUser reactivateUser;

    @Override
    public Result<List<EventNotification>> reactivate(UUID userId) {
        var maybeUser = queryRepository.findByUuid(userId);
        if (maybeUser.isEmpty()) return Result.failure(AppError.idNotFound(userId, "User"));

        var user = maybeUser.get();
        if (user.isSuspended()) return Result.failure(AppError.INVALID_OPERATION
                    .withMessage("is suspended and cannot be reactivated"));

        return reactivateUser.reactivate(user);
    }
}
