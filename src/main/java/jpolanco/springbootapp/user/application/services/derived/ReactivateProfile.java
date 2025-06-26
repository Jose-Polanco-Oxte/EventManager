package jpolanco.springbootapp.user.application.services.derived;

import jpolanco.springbootapp.shared.application.AppError;
import jpolanco.springbootapp.shared.domain.Report;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.uc.base.ReactivateUserUC;
import jpolanco.springbootapp.user.application.uc.derived.ReactivateProfileUC;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReactivateProfile implements ReactivateProfileUC {
    private final UserQueryRepository queryRepository;
    private final ReactivateUserUC reactivateUserUC;

    @Override
    public Report reactivate(String userId) {
        var maybeUser = queryRepository.findById(userId);
        if (maybeUser.isEmpty()) return Report.failure(AppError.idNotFound(userId, "User"));

        var user = maybeUser.get();
        if (user.isSuspended()) return Report.failure(AppError.INVALID_OPERATION
                    .withMessage("is suspended and cannot be reactivated"));

        return reactivateUserUC.reactivate(user);
    }
}
