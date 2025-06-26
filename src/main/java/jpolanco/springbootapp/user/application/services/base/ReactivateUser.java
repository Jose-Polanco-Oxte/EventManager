package jpolanco.springbootapp.user.application.services.base;

import jpolanco.springbootapp.shared.application.AppError;
import jpolanco.springbootapp.shared.domain.Report;
import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.uc.base.ReactivateUserUC;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.domain.model.UserUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReactivateUser implements ReactivateUserUC {
    private final UserQueryRepository queryRepository;
    private final UserCommandRepository commandRepository;

    @Override
    public Report reactivate(User user) {
        var report = UserUpdater.updater(user)
                .reactivate()
                .update();
        if (report.isFailure()) return report;

        commandRepository.save(user);
        return report;
    }

    @Override
    public Report reactivateById(String userId) {
        var maybeUser = queryRepository.findById(userId);
        if (maybeUser.isEmpty()) return Report.failure(AppError.idNotFound(userId, "User"));

        var user = maybeUser.get();
        return reactivate(user);
    }
}
