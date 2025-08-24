package jpolanco.applicationcore.user.application.services.implementation.commands;

import jpolanco.applicationcore.audit.utils.Audit;
import jpolanco.applicationcore.shared.application.errors.InvalidOperation;
import jpolanco.applicationcore.shared.application.errors.NotFound;
import jpolanco.applicationcore.shared.application.exceptions.ApplicationExceptionHandler;
import jpolanco.applicationcore.shared.application.utils.EventBus;
import jpolanco.applicationcore.shared.domain.notify.EventNotification;
import jpolanco.applicationcore.shared.domain.utils.abstracterrors.ServiceError;
import jpolanco.applicationcore.shared.domain.utils.primitives.results.Result;
import jpolanco.applicationcore.user.application.errors.AccountError;
import jpolanco.applicationcore.user.application.events.UserLogged;
import jpolanco.applicationcore.user.application.ports.input.AuthProvider;
import jpolanco.applicationcore.user.application.ports.input.TokenGenerator;
import jpolanco.applicationcore.user.application.ports.output.TokenQueryRepository;
import jpolanco.applicationcore.user.application.ports.output.UserQueryRepository;
import jpolanco.applicationcore.user.application.services.interfaces.commands.Login;
import jpolanco.applicationcore.user.domain.model.User;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request.LoginRequest;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.response.UserTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginDefault implements Login {

    private final AuthProvider authentication;

    private final UserQueryRepository userQueryRepository;

    private final TokenQueryRepository tokenQueryRepository;

    private final TokenGenerator tokenService;

    private final EventBus<EventNotification> publisher;

    @Audit(action = OPERATION)
    @Transactional
    @Override
    public UserTokenResponse loginUser(LoginRequest request) {
        authentication.authenticate(request.email(), request.password());
        Optional<User> maybeUser = userQueryRepository.findByEmailUserFilter(request.email());

        if (maybeUser.isEmpty()) {
            throw new ApplicationExceptionHandler(NotFound.resourceNotFound("User", "email", request.email()));
        }

        User user = maybeUser.get();

        if (!user.isActive()) {
            throw new ApplicationExceptionHandler(InvalidOperation.invalidActionError("User is inactive", "REACTIVATE"));
        }

        if (user.isSuspended()) {
            throw new ApplicationExceptionHandler(InvalidOperation.invalidActionError("User is suspended"));
        }

        int sessions = tokenQueryRepository.countByUserId(user.getUserId().getUUID());

        if (sessions >= 5) {
            throw new ApplicationExceptionHandler(AccountError.SessionLimitExceeded(5));
        }

        Result<UserTokenResponse, ServiceError> resultTokens = tokenService.generateToken(user);

        if (resultTokens.isFailure()) {
            throw new ApplicationExceptionHandler(resultTokens.getErrors());
        }

        UserTokenResponse tokens = resultTokens.getValue();

        publisher.publish(new UserLogged(
                user.getUserId().getUUID(),
                user.getEmail().getValue()
        ));

        return tokens;
    }
}
