package jpolanco.applicationcore.user.application.services.implementation.commands;

import jpolanco.applicationcore.audit.utils.Audit;
import jpolanco.applicationcore.shared.application.errors.InvalidOperation;
import jpolanco.applicationcore.shared.application.errors.NotFound;
import jpolanco.applicationcore.shared.application.errors.Unauthorized;
import jpolanco.applicationcore.shared.application.exceptions.ApplicationExceptionHandler;
import jpolanco.applicationcore.shared.application.utils.EventBus;
import jpolanco.applicationcore.shared.domain.notify.EventNotification;
import jpolanco.applicationcore.shared.domain.utils.abstracterrors.ServiceError;
import jpolanco.applicationcore.shared.domain.utils.primitives.results.Result;
import jpolanco.applicationcore.user.application.events.UserTokenRefreshed;
import jpolanco.applicationcore.user.application.ports.input.TokenGenerator;
import jpolanco.applicationcore.user.application.ports.output.TokenCommandRepository;
import jpolanco.applicationcore.user.application.ports.output.TokenQueryRepository;
import jpolanco.applicationcore.user.application.ports.output.UserQueryRepository;
import jpolanco.applicationcore.user.application.services.interfaces.commands.Refresh;
import jpolanco.applicationcore.user.domain.model.User;
import jpolanco.applicationcore.user.domain.model.UserStatus;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.response.UserTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshDefault implements Refresh {

    private final TokenGenerator tokenService;

    private final EventBus<EventNotification> publisher;

    private final UserQueryRepository userQueryRepository;

    private final TokenCommandRepository tokenCommandRepository;

    private final TokenQueryRepository tokenQueryRepository;

    @Audit(action = OPERATION)
    @Transactional
    @Override
    public UserTokenResponse refresh(String refreshToken) {
        if (refreshToken == null) {
            throw new ApplicationExceptionHandler(
                    Unauthorized.unauthorizedError("Missing authorization header"));
        }

        if (!tokenQueryRepository.tokenIsValid(refreshToken)) {
            throw new ApplicationExceptionHandler(
                    Unauthorized.unauthorizedError("Invalid or expired refresh token"));
        }

        Result<String, ServiceError> resultExtractEmail = tokenService.extractUsername(refreshToken);

        if (resultExtractEmail.isFailure()) {
            throw new ApplicationExceptionHandler(resultExtractEmail.getErrors());
        }

        String userEmail = resultExtractEmail.getValue();

        Optional<User> maybeUser = userQueryRepository.findByEmailUserFilter(userEmail);

        if (maybeUser.isEmpty()) {
            throw new ApplicationExceptionHandler(NotFound.resourceNotFound("User", "email", userEmail));
        }

        User user = maybeUser.get();

        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new ApplicationExceptionHandler(InvalidOperation.invalidActionError("User is inactive"));
        }

        if (user.getStatus() == UserStatus.SUSPENDED) {
            throw new ApplicationExceptionHandler(InvalidOperation.invalidActionError("User is suspended"));
        }

        tokenCommandRepository.revokeByToken(refreshToken);

        Result<UserTokenResponse, ServiceError> resultTokens = tokenService.generateToken(user);

        if (resultTokens.isFailure()) {
            throw new ApplicationExceptionHandler(resultTokens.getErrors());
        }

        UserTokenResponse tokens = resultTokens.getValue();

        publisher.publish(new UserTokenRefreshed(
                user.getUserId().getUUID(),
                user.getEmail().getValue(),
                refreshToken,
                tokens.refreshToken()
        ));

        return tokens;
    }
}
