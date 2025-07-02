package jpolanco.springbootapp.user.application.services.base;

import jpolanco.springbootapp.shared.application.AppError;
import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.application.ports.input.QRProvider;
import jpolanco.springbootapp.user.application.ports.output.JwtCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.uc.base.DeleteUserUC;
import jpolanco.springbootapp.user.application.application_events.UserDeleted;
import jpolanco.springbootapp.user.domain.model.value_objects.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteUser implements DeleteUserUC {
    private final UserQueryRepository queryRepository;
    private final UserCommandRepository commandRepository;
    private final JwtCommandRepository jwtCommandRepository;
    private final QRProvider qrProvider;

    @Override
    public Result<List<EventNotification>> delete(User user, String reason) {
        jwtCommandRepository.deleteAllByUserId(user.getId());
        commandRepository.deleteById(user.getId());
        qrProvider.delete(user.getQRFileName());
        return Result.success(List.of(new UserDeleted(user.getUUID(), reason)));
    }

    @Override
    public Result<List<EventNotification>> deleteById(UUID userId, String reason) {
        var maybeUser = queryRepository.findByUuid(userId);
        if (maybeUser.isEmpty()) return Result.failure(AppError.idNotFound(userId, "User"));

        var user = maybeUser.get();
        return delete(user, reason);
    }
}
