package jpolanco.springbootapp.user.application.services.derived;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.application.errors.UserAppError;
import jpolanco.springbootapp.user.application.ports.output.UserRepository;
import jpolanco.springbootapp.user.application.uc.derived.UpdateProfileNameUC;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.UpdateNameRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateProfileName implements UpdateProfileNameUC {
    private final UserRepository userRepository;

    @Override
    public Result<User> setName(String userId, UpdateNameRequest updateNameRequest) {
        var maybeUser = userRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            return Result.failure(UserAppError.USER_NOT_FOUND);
        }
        var user = maybeUser.get();
        if (user.isSuspended()) {
            return Result.failure(UserAppError.USER_SUSPENDED);
        }
        user.changeFirstName(updateNameRequest.firstName());
        user.changeLastName(updateNameRequest.lastName());
        var updatedUser = userRepository.save(user);
        return Result.success(updatedUser);
    }
}
