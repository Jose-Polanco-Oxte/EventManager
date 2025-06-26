package jpolanco.springbootapp.user.application.services.derived;

import jpolanco.springbootapp.shared.application.AppError;
import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.uc.base.DeleteUserUC;
import jpolanco.springbootapp.user.application.uc.derived.DeleteProfileUC;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeleteProfile implements DeleteProfileUC {
    private final UserQueryRepository queryRepository;
    private final DeleteUserUC deleteUserUC;

    @Override
    public Result<List<EventNotification>> delete(String userId, String reason) {
        var maybeUser = queryRepository.findById(userId);
        if (maybeUser.isEmpty()) return Result.failure(AppError.idNotFound(userId, "User"));

        var user = maybeUser.get();
        if (user.isSuspended()) return Result.failure(AppError.INVALID_OPERATION
                    .withMessage("is suspended and cannot be deleted"));

        return deleteUserUC.delete(user, reason);
    }
}
