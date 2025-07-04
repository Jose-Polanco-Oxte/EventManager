package jpolanco.springbootapp.user.application.defaultservices.base;

import jpolanco.springbootapp.shared.application.AppError;
import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.utils.results.Result;
import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.usecase.base.SuspendUser;
import jpolanco.springbootapp.user.domain.model.valueobjects.User;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class SuspendUserDefault implements SuspendUser {
    private final UserQueryRepository queryRepository;
    private final UserCommandRepository commandRepository;

    @Override
    public Result<List<EventNotification>> suspend(User user, String reason) {
        user.suspend(reason);
        var savedUser = commandRepository.save(user);
        return Result.success(savedUser.pullEvents());
    }

    @Override
    public Result<List<EventNotification>> suspendById(UUID userId, String reason) {
        var maybeUser = queryRepository.findByUuid(userId);
        if (maybeUser.isEmpty()) return Result.failure(AppError.idNotFound(userId, "User"));

        var user = maybeUser.get();
        return suspend(user, reason);
    }
}
