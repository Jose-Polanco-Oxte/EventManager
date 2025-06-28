package jpolanco.springbootapp.user.application.services.base;

import jpolanco.springbootapp.shared.application.AppError;
import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.application.ports.input.QRProvider;
import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.uc.base.DeleteUserUC;
import jpolanco.springbootapp.user.application.application_events.UserDeleted;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.infrastructure.adapters.output.repository.JpaTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteUser implements DeleteUserUC {
    private final UserQueryRepository queryRepository;
    private final UserCommandRepository commandRepository;
    private final JpaTokenRepository jpaTokenRepository;
    private final QRProvider qrProvider;

    @Override
    public Result<List<EventNotification>> delete(User user, String reason) {
        jpaTokenRepository.deleteAllByUserId(UUID.fromString(user.getId()));
        qrProvider.delete(user.getQRFileName());
        commandRepository.deleteById(user.getId());
        return Result.success(List.of(new UserDeleted(user.getId(), reason)));
    }

    @Override
    public Result<List<EventNotification>> deleteById(String userId, String reason) {
        var maybeUser = queryRepository.findById(userId);
        if (maybeUser.isEmpty()) return Result.failure(AppError.idNotFound(userId, "User"));

        var user = maybeUser.get();
        return delete(user, reason);
    }
}
