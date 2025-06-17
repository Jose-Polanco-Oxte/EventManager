package jpolanco.springbootapp.user.application.services;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.application.errors.UserAppError;
import jpolanco.springbootapp.user.application.ports.output.UserRepository;
import jpolanco.springbootapp.user.application.uc.LoginUC;
import jpolanco.springbootapp.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class Login implements LoginUC {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Result<User> login(String email, String password) {
        var maybeUser = userRepository.findByEmail(email);
        if (maybeUser.isEmpty()) {
            return Result.failure(UserAppError.USER_NOT_FOUND);
        }
        var user = maybeUser.get();
        if (user.isInactive()) {
            return Result.failure(UserAppError.USER_NOT_ACTIVE);
        }
        if (user.isSuspended()) {
            return Result.failure(UserAppError.USER_SUSPENDED);
        }
        if (!passwordEncoder.matches(password, user.getEncodedPassword())) {
            return Result.failure(UserAppError.USER_NOT_AUTHORIZED);
        }
        return Result.success(user);
    }
}
