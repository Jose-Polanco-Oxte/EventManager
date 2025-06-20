package jpolanco.springbootapp.user.application.services.unique;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.application.errors.UserAppError;
import jpolanco.springbootapp.user.application.ports.output.JwtRepository;
import jpolanco.springbootapp.user.application.ports.output.UserRepository;
import jpolanco.springbootapp.user.application.uc.unique.LoginUC;
import jpolanco.springbootapp.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Login implements LoginUC {
    private final UserRepository userRepository;
    private final JwtRepository jwtRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Result<User> login(String email, String password) {
        var maybeUser = userRepository.findByEmail(email);
        if (maybeUser.isEmpty()) {
            return Result.failure(UserAppError.USER_NOT_FOUND);
        }
        var user = maybeUser.get();
        int sessions = jwtRepository.countSessionsByUserId(user.getId());
        if (sessions >= 5) {
            return Result.failure(UserAppError.SESSION_LIMIT_REACHED);
        }
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
