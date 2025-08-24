package jpolanco.applicationcore.user.application.services.implementation.commands;

import jakarta.transaction.Transactional;
import jpolanco.applicationcore.audit.utils.Audit;
import jpolanco.applicationcore.shared.application.errors.Internal;
import jpolanco.applicationcore.shared.application.errors.InvalidOperation;
import jpolanco.applicationcore.shared.application.errors.NotFound;
import jpolanco.applicationcore.shared.application.errors.Unauthorized;
import jpolanco.applicationcore.shared.application.exceptions.ApplicationExceptionHandler;
import jpolanco.applicationcore.shared.application.exceptions.DomainExceptionHandler;
import jpolanco.applicationcore.shared.domain.utils.abstracterrors.DomainError;
import jpolanco.applicationcore.shared.domain.utils.primitives.results.Result;
import jpolanco.applicationcore.user.application.errors.AccountError;
import jpolanco.applicationcore.user.application.mappers.UserDto;
import jpolanco.applicationcore.user.application.ports.input.EncoderProvider;
import jpolanco.applicationcore.user.application.services.interfaces.commands.ProfileChanges;
import jpolanco.applicationcore.user.domain.model.User;
import jpolanco.applicationcore.user.domain.repositories.UserRepository;
import jpolanco.applicationcore.user.domain.services.UserUpdatePolicyService;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request.ChangeEmailRequest;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request.ChangeNameRequest;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request.ChangePasswordRequest;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileChangesDefault implements ProfileChanges {

    private final UserRepository userRepository;

    private final UserUpdatePolicyService userUpdatePolicyService;

    private final EncoderProvider encoderProvider;

    @Audit(action = "CHANGE_EMAIL")
    @Transactional
    @Override
    public UserResponse changeEmail(UUID userId, ChangeEmailRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationExceptionHandler(NotFound.entityNotFound("User", request.email())));

        if (!request.email().equals(request.confirmEmail())) {
            throw new ApplicationExceptionHandler(InvalidOperation.invalidActionError("Email and confirm email must match"));
        }

        if (user.getEmail().getValue().equals(request.email())) {
            throw new ApplicationExceptionHandler(InvalidOperation.invalidActionError("New email must be different from the current one"));
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new ApplicationExceptionHandler(AccountError.EmailAlreadyInUse(request.email()));
        }

        Result<Void, DomainError> checkUpdatePolicy = userUpdatePolicyService.checkAbleToUpdateSelf(user);

        if (checkUpdatePolicy.isFailure()) {
            throw new DomainExceptionHandler(checkUpdatePolicy.getErrors());
        }

        Result<Void, DomainError> result = user.changeEmail(request.email());

        if (result.isFailure()) {
            throw new DomainExceptionHandler(result.getErrors());
        }

        User updatedUser = userRepository.save(user);

        if (updatedUser == null) {
            throw new ApplicationExceptionHandler(Internal.internalError("Failed to update user email"));
        }

        return UserDto.toResponse(updatedUser);
    }

    @Audit(action = "CHANGE_NAME")
    @Transactional
    @Override
    public UserResponse changeName(UUID userId, ChangeNameRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationExceptionHandler(NotFound.entityNotFound("User", userId.toString())));

        Result<Void, DomainError> checkUpdatePolicy = userUpdatePolicyService.checkAbleToUpdateSelf(user);

        if (checkUpdatePolicy.isFailure()) {
            throw new DomainExceptionHandler(checkUpdatePolicy.getErrors());
        }

        Result<Void, DomainError> result = user.changeFirstName(request.firstName())
                .andThen(r -> user.changeLastName(request.lastName()));

        if (result.isFailure()) {
            throw new DomainExceptionHandler(result.getErrors());
        }

        User updatedUser = userRepository.save(user);

        if (updatedUser == null) {
            throw new ApplicationExceptionHandler(Internal.internalError("Failed to update user name"));
        }

        return UserDto.toResponse(updatedUser);
    }

    @Audit(action = "CHANGE_PASSWORD")
    @Transactional
    @Override
    public UserResponse changePassword(UUID userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationExceptionHandler(NotFound.entityNotFound("User", userId.toString())));

        Result<Void, DomainError> checkUpdatePolicy = userUpdatePolicyService.checkAbleToUpdateSelf(user);

        if (checkUpdatePolicy.isFailure()) {
            throw new DomainExceptionHandler(checkUpdatePolicy.getErrors());
        }

        if (!encoderProvider.matches(request.oldPassword(), user.getEncodedPassword().getValue())) {
            throw new ApplicationExceptionHandler(Unauthorized.unauthorizedError("Old password does not match"));
        }

        if (encoderProvider.matches(request.newPassword(), user.getEncodedPassword().getValue())) {
            throw new ApplicationExceptionHandler(InvalidOperation.invalidActionError("New password must be different from the current one"));
        }

        Result<Void, DomainError> result = user.changeEncodedPassword(encoderProvider.encode(request.newPassword()));

        if (result.isFailure()) {
            throw new DomainExceptionHandler(result.getErrors());
        }

        User updatedUser = userRepository.save(user);

        if (updatedUser == null) {
            throw new ApplicationExceptionHandler(Internal.internalError("Failed to update user password"));
        }

        return UserDto.toResponse(updatedUser);
    }
}
