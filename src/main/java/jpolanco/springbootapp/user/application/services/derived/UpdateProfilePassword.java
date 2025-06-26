package jpolanco.springbootapp.user.application.services.derived;

import jpolanco.springbootapp.shared.application.AppError;
import jpolanco.springbootapp.shared.domain.Report;
import jpolanco.springbootapp.user.application.ports.input.EncoderProvider;
import jpolanco.springbootapp.user.application.ports.output.JwtCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.uc.derived.UpdateProfilePasswordUC;
import jpolanco.springbootapp.user.domain.model.UserUpdater;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.UpdatePasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateProfilePassword implements UpdateProfilePasswordUC {
    private final UserQueryRepository queryRepository;
    private final UserCommandRepository commandRepository;
    private final JwtCommandRepository jwtCommandRepository;
    private final EncoderProvider passwordEncoder;

    @Override
    public Report setPassword(String userId, UpdatePasswordRequest request) {
        var maybeUser = queryRepository.findById(userId);
        if (maybeUser.isEmpty()) return Report.failure(AppError.idNotFound(userId, "User"));

        var user = maybeUser.get();
        if (user.isInactive()) return Report.failure(AppError.INVALID_OPERATION
                    .withMessage("Cannot update password of an inactive user."));

        if (user.isSuspended()) return Report.failure(AppError.INVALID_OPERATION
                .withMessage("Cannot update password of an inactive user."));

        if (!passwordEncoder.matches(request.oldPassword(), user.getEncodedPassword())) {
            return Report.failure(AppError.UNAUTHORIZED
                    .withMessage("old password does not match the current password."));
        }

        if (user.isSuspended()) return Report.failure(AppError.INVALID_OPERATION
                .withMessage("Cannot update password of an inactive user."));

        var encodedPassword = passwordEncoder.encode(request.newPassword());

        var report = UserUpdater.updater(user)
                .password(encodedPassword)
                .update();
        if (report.isFailure()) return report;

        var updatedUser = commandRepository.save(user);
        jwtCommandRepository.revokeAllByUserId(updatedUser.getId());
        return report;
    }
}
