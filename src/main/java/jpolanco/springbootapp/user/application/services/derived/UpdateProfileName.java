package jpolanco.springbootapp.user.application.services.derived;

import jpolanco.springbootapp.shared.application.AppError;
import jpolanco.springbootapp.shared.domain.UpdateReport;
import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.uc.derived.UpdateProfileNameUC;
import jpolanco.springbootapp.user.domain.model.UserUpdater;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.ChangeNameRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateProfileName implements UpdateProfileNameUC {
    private final UserQueryRepository queryRepository;
    private final UserCommandRepository commandRepository;

    @Override
    public UpdateReport setName(UUID userId, ChangeNameRequest request) {
        var maybeUser = queryRepository.findByUuid(userId);
        if (maybeUser.isEmpty()) return UpdateReport.failure(AppError.idNotFound(userId, "User"));

        var user = maybeUser.get();
        if (user.isSuspended()) return UpdateReport.failure(AppError.INVALID_OPERATION
                .withMessage("Cannot update name invoke a suspended userId."));

        var report = UserUpdater.updater(user)
                .firstName(request.firstName())
                .lastName(request.lastName())
                .update();
        if (report.isFailure()) return report;

        commandRepository.save(user);
        return report;
    }
}
