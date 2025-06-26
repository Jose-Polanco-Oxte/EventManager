package jpolanco.springbootapp.user.application.services.base;

import jpolanco.springbootapp.shared.application.AppError;
import jpolanco.springbootapp.shared.domain.Report;
import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.uc.base.SuspendUserUC;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.domain.model.UserUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SuspendUser implements SuspendUserUC {
    private final UserQueryRepository queryRepository;
    private final UserCommandRepository commandRepository;

    @Override
    public Report suspend(User user, String reason) {
        var report = UserUpdater.updater(user)
                .suspend(reason)
                .update();
        if (report.isFailure()) return report;

        commandRepository.save(user);
        return report;
    }

    @Override
    public Report suspendById(String userId, String reason) {
        var maybeUser = queryRepository.findById(userId);
        if (maybeUser.isEmpty()) return Report.failure(AppError.idNotFound(userId, "User"));

        var user = maybeUser.get();
        return suspend(user, reason);
    }
}
