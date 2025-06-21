package jpolanco.springbootapp.user.application.services.base;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.application.errors.UserAppError;
import jpolanco.springbootapp.user.application.ports.input.QRProvider;
import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.uc.base.DeleteUserUC;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.infrastructure.adapters.output.repository.JpaTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteUser implements DeleteUserUC {
    private final UserQueryRepository queryRepository;
    private final UserCommandRepository commandRepository;
    private final JpaTokenRepository jpaTokenRepository;
    private final QRProvider qrProvider;

    @Override
    public Result<Void> delete(User user) {
        jpaTokenRepository.deleteAllByUserId(UUID.fromString(user.getId()));
        qrProvider.delete(user.getQRFileName());
        commandRepository.deleteById(user.getId());
        return Result.success();
    }

    @Override
    public Result<Void> deleteById(String userId) {
        var maybeUser = queryRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            return Result.failure(UserAppError.USER_NOT_FOUND);
        }
        var user = maybeUser.get();

        // Check if the user has any tokens associated with them
        jpaTokenRepository.deleteAllByUserId(UUID.fromString(userId));
        qrProvider.delete(user.getQRFileName());
        commandRepository.deleteById(userId);
        return Result.success();
    }
}
