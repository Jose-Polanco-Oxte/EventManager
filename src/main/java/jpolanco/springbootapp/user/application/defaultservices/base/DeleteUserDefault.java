package jpolanco.springbootapp.user.application.defaultservices.base;

import jpolanco.springbootapp.shared.application.AppError;
import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.utils.results.Result;
import jpolanco.springbootapp.user.application.ports.input.QRProvider;
import jpolanco.springbootapp.user.application.ports.output.JwtCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.usecase.base.DeleteUser;
import jpolanco.springbootapp.user.application.appevents.UserDeleted;
import jpolanco.springbootapp.user.domain.model.valueobjects.User;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class DeleteUserDefault implements DeleteUser {
    private final UserQueryRepository queryRepository;
    private final UserCommandRepository commandRepository;
    private final JwtCommandRepository jwtCommandRepository;
    private final QRProvider qrProvider;

    @Override
    public Result<List<EventNotification>> delete(User user, String reason) {
        jwtCommandRepository.deleteAllByUserId(user.getId());
        commandRepository.deleteById(user.getId());
        qrProvider.delete(user.getQRFileName());
        return Result.success(new ArrayList<>(List.of(new UserDeleted(user.getUUID(), reason))));
    }

    @Override
    public Result<List<EventNotification>> deleteById(UUID userId, String reason) {
        var maybeUser = queryRepository.findByUuid(userId);
        if (maybeUser.isEmpty()) return Result.failure(AppError.idNotFound(userId, "User"));

        var user = maybeUser.get();
        return delete(user, reason);
    }
}
