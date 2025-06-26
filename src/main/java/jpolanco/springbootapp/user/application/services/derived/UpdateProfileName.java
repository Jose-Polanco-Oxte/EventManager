package jpolanco.springbootapp.user.application.services.derived;

import jpolanco.springbootapp.shared.application.AppError;
import jpolanco.springbootapp.shared.domain.Report;
import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.uc.derived.UpdateProfileNameUC;
import jpolanco.springbootapp.user.domain.model.UserUpdater;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.UpdateNameRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateProfileName implements UpdateProfileNameUC {
    private final UserQueryRepository queryRepository;
    private final UserCommandRepository commandRepository;

    @Override
    public Report setName(String userId, UpdateNameRequest request) {
        var maybeUser = queryRepository.findById(userId);
        if (maybeUser.isEmpty()) return Report.failure(AppError.idNotFound(userId, "User"));

        var user = maybeUser.get();
        if (user.isSuspended()) return Report.failure(AppError.INVALID_OPERATION
                .withMessage("Cannot update name of a suspended user."));

        var report = UserUpdater.updater(user)
                .firstName(request.firstName())
                .lastName(request.lastName())
                .update();
        if (report.isFailure()) return report;

        commandRepository.save(user);
        return report;
    }
}
