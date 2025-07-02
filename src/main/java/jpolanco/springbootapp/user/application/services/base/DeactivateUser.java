package jpolanco.springbootapp.user.application.services.base;

import jpolanco.springbootapp.shared.application.AppError;
import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.uc.base.DeactivateUserUC;
import jpolanco.springbootapp.user.domain.model.value_objects.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeactivateUser implements DeactivateUserUC {
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
