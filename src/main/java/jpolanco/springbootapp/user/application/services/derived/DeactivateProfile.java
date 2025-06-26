package jpolanco.springbootapp.user.application.services.derived;

import jpolanco.springbootapp.shared.application.AppError;
import jpolanco.springbootapp.shared.domain.Report;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.uc.base.DeactivateUserUC;
import jpolanco.springbootapp.user.application.uc.derived.DeactivateProfileUC;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeactivateProfile implements DeactivateProfileUC {
    private final UserQueryRepository queryRepository;
    private final DeactivateUserUC deactivateUserUC;

    @Override
    public Report deactivate(String userId, String reason) {
        var maybeUser = queryRepository.findById(userId);
        if (maybeUser.isEmpty()) return Report.failure(AppError.idNotFound(userId, "User"));

        var user = maybeUser.get();
        if (user.isSuspended()) return Report.failure(AppError.INVALID_OPERATION
                .withMessage("user is suspended and cannot be deactivated"));

        return deactivateUserUC.deactivate(user, reason);
    }
}
