package jpolanco.springbootapp.user.application.defaultservices.derived;

import jpolanco.springbootapp.shared.application.AppError;
import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.utils.results.Result;
import jpolanco.springbootapp.user.application.ports.input.EncoderProvider;
import jpolanco.springbootapp.user.application.ports.output.JwtCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.usecase.derived.UpdateAccountPassword;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.ChangePasswordRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class UpdateAccountPasswordDefault implements UpdateAccountPassword {
    private final UserQueryRepository queryRepository;
    private final UserCommandRepository commandRepository;
    private final JwtCommandRepository jwtCommandRepository;
    private final EncoderProvider passwordEncoder;

    @Override
    public Result<List<EventNotification>> setPassword(UUID userId, ChangePasswordRequest request) {
        var maybeUser = queryRepository.findByUuid(userId);
        if (maybeUser.isEmpty()) return Result.failure(AppError.idNotFound(userId, "User"));

        var user = maybeUser.get();
        if (user.isInactive()) return Result.failure(AppError.INVALID_OPERATION
                    .withMessage("Cannot update password invoke an inactive userId."));

        if (user.isSuspended()) return Result.failure(AppError.INVALID_OPERATION
                .withMessage("Cannot update password invoke an inactive userId."));

        if (!passwordEncoder.matches(request.oldPassword(), user.getEncodedPassword())) {
            return Result.failure(AppError.UNAUTHORIZED
                    .withMessage("old password does not match the current password."));
        }

        if (user.isSuspended()) return Result.failure(AppError.INVALID_OPERATION
                .withMessage("Cannot update password invoke an inactive userId."));

        var encodedPassword = passwordEncoder.encode(request.newPassword());

        var result = user.changeEncodedPassword(encodedPassword);
        if (result.isFailure()) return Result.failure(result.getError());

        var savedUser = commandRepository.save(user);
        jwtCommandRepository.revokeAllByUserId(savedUser.getId());
        return Result.success(savedUser.pullEvents());
    }
}
