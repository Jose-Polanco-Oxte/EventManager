package jpolanco.applicationcore.user.application.services.implementation.commands;

import jpolanco.applicationcore.audit.utils.Audit;
import jpolanco.applicationcore.shared.application.errors.Internal;
import jpolanco.applicationcore.shared.application.exceptions.ApplicationExceptionHandler;
import jpolanco.applicationcore.shared.application.exceptions.DomainExceptionHandler;
import jpolanco.applicationcore.shared.application.utils.EventBus;
import jpolanco.applicationcore.shared.domain.notify.EventNotification;
import jpolanco.applicationcore.shared.domain.utils.abstracterrors.DomainError;
import jpolanco.applicationcore.shared.domain.utils.abstracterrors.ServiceError;
import jpolanco.applicationcore.shared.domain.utils.primitives.results.Result;
import jpolanco.applicationcore.user.application.errors.AccountError;
import jpolanco.applicationcore.user.application.ports.input.EncoderProvider;
import jpolanco.applicationcore.user.application.ports.input.QRProvider;
import jpolanco.applicationcore.user.application.ports.input.TokenGenerator;
import jpolanco.applicationcore.user.application.services.interfaces.commands.RegisterAccount;
import jpolanco.applicationcore.user.domain.model.User;
import jpolanco.applicationcore.user.domain.model.UserRoles;
import jpolanco.applicationcore.user.domain.repositories.UserRepository;
import jpolanco.applicationcore.user.domain.services.PasswordStrongestPolicyService;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request.RegisterRequest;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.response.UserTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegisterAccountDefault implements RegisterAccount {

    private final UserRepository userRepository;

    private final TokenGenerator tokenService;

    private final EventBus<EventNotification> publisher;

    private final EncoderProvider encoder;

    private final QRProvider qrProvider;

    private final PasswordStrongestPolicyService passwordStrongestPolicyService;

    @Audit(action = OPERATION)
    @Override
    @Transactional
    public UserTokenResponse registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ApplicationExceptionHandler(
                    AccountError.EmailAlreadyInUse(request.email()));
        }

        Result<Void, DomainError> checkPasswordPolicy = passwordStrongestPolicyService.check(request.password());

        if (checkPasswordPolicy.isFailure()) {
            throw new DomainExceptionHandler(checkPasswordPolicy.getErrors());
        }

        String encodedPassword = request.password() != null ? encoder.encode(request.password()) : null;

        Result<User, DomainError> result = User.create(
                request.firstName(),
                request.lastName(),
                request.email(),
                encodedPassword,
                List.of(UserRoles.USER.getValue())
        );

        if (result.isFailure()) {
            throw new DomainExceptionHandler(result.getErrors());
        }

        User newUser = result.getValue();

        Result<UserTokenResponse, ServiceError> resultToken = tokenService.generateToken(newUser);

        if (resultToken.isFailure()) {
            throw new ApplicationExceptionHandler(resultToken.getErrors());
        }

        UserTokenResponse token = resultToken.getValue();

        User newSavedUser = userRepository.save(newUser);

        if (newSavedUser == null) {
            throw new ApplicationExceptionHandler(Internal.internalError("Failed to save new user"));
        }
        
        Result<Void, ServiceError> qrResult = qrProvider.generate(newSavedUser.getQR());

        if (qrResult.isFailure()) {
            throw new ApplicationExceptionHandler(qrResult.getErrors());
        }

        publisher.publishAll(newSavedUser.pullEvents());

        return token;
    }

    @Audit(action = OPERATION)
    public UserTokenResponse registerAdmin(RegisterRequest request) {
        return null;
    }
}
