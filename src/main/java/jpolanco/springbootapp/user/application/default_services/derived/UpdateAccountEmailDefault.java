package jpolanco.springbootapp.user.application.default_services.derived;

import jpolanco.springbootapp.shared.application.AppError;
import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.application.ports.output.JwtCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.use_case.derived.UpdateProfileEmailUC;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.ChangeEmailRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class UpdateProfileEmail implements UpdateProfileEmailUC {
    private final UserQueryRepository queryRepository;
    private final UserCommandRepository commandRepository;
    private final JwtCommandRepository jwtCommandRepository;

    @Override
    public Result<List<EventNotification>> setEmail(UUID userId, ChangeEmailRequest request) {
        var OptionalUser = queryRepository.findByUuid(userId);
        if (OptionalUser.isEmpty()) return Result.failure(AppError.idNotFound(userId, "User"));

        var user = OptionalUser.get();
        if (queryRepository.findByEmail(request.email()).isPresent()
                && !user.getEmail().equals(request.email())) {
            return Result.failure(AppError.CONFLICT
                    .withField("Email")
                    .concatMessage("with " + request.email() + " is already in use"));
        }

        if (user.isSuspended()) return Result.failure(AppError.INVALID_OPERATION
                .withMessage("cannot be changed while the userId is suspended"));

        var result = user.changeEmail(request.email());
        if (result.isFailure()) return Result.failure(result.getError());

        var savedUser = commandRepository.save(user);
        jwtCommandRepository.revokeAllByUserId(savedUser.getId());
        return Result.success(savedUser.pullEvents());
    }
}
