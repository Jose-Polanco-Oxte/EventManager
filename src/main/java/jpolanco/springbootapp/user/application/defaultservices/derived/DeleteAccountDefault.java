package jpolanco.springbootapp.user.application.defaultservices.derived;

import jpolanco.springbootapp.shared.application.AppError;
import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.utils.results.Result;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.usecase.base.DeleteUser;
import jpolanco.springbootapp.user.application.usecase.derived.DeleteAccount;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class DeleteAccountDefault implements DeleteAccount {
    private final UserQueryRepository queryRepository;
    private final DeleteUser deleteUser;

    @Override
    public Result<List<EventNotification>> delete(UUID userId, String reason) {
        var maybeUser = queryRepository.findByUuid(userId);
        if (maybeUser.isEmpty()) return Result.failure(AppError.idNotFound(userId, "User"));

        var user = maybeUser.get();
        if (user.isSuspended()) return Result.failure(AppError.INVALID_OPERATION
                    .withMessage("is suspended and cannot be deleted"));

        return deleteUser.delete(user, reason);
    }
}
