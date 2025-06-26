package jpolanco.springbootapp.user.application.services.derived;

import jpolanco.springbootapp.shared.application.AppError;
import jpolanco.springbootapp.shared.domain.Report;
import jpolanco.springbootapp.user.application.ports.output.JwtCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.uc.derived.UpdateProfileEmailUC;
import jpolanco.springbootapp.user.domain.model.UserUpdater;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.UpdateEmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateProfileEmail implements UpdateProfileEmailUC {
    private final UserQueryRepository queryRepository;
    private final UserCommandRepository commandRepository;
    private final JwtCommandRepository jwtCommandRepository;

    @Override
    public Report setEmail(String userId, UpdateEmailRequest request) {
        var OptionalUser = queryRepository.findById(userId);
        if (OptionalUser.isEmpty()) return Report.failure(AppError.idNotFound(userId, "User"));

        var user = OptionalUser.get();
        if (queryRepository.findByEmail(request.email()).isPresent()
                && !user.getEmail().equals(request.email())) {
            return Report.failure(AppError.CONFLICT
                    .withField("Email")
                    .concatMessage("with " + request.email() + " is already in use"));
        }

        if (user.isSuspended()) return Report.failure(AppError.INVALID_OPERATION
                .withMessage("cannot be changed while the user is suspended"));

        var report = UserUpdater.updater(user)
                .email(request.email())
                .update();
        if (report.isFailure()) return report;

        var savedUser = commandRepository.save(user);
        jwtCommandRepository.revokeAllByUserId(savedUser.getId());
        return report;
    }
}
