package jpolanco.springbootapp.user.application.services;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.application.errors.UserAppError;
import jpolanco.springbootapp.user.application.ports.input.QRProvider;
import jpolanco.springbootapp.user.application.ports.output.UserRepository;
import jpolanco.springbootapp.user.application.utils.UserValidation;
import jpolanco.springbootapp.user.application.uc.CreateUserUC;
import jpolanco.springbootapp.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreateUser implements CreateUserUC {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final QRProvider qrProvider;
    private final UserValidation userValidation;

    @Override
    public Result<User> create(String firstName, String lastName, String email, String password) {
        if (userValidation.isEmailExists(email)) {
            return Result.failure(UserAppError.USER_ALREADY_EXISTS);
        }
        var encodedPassword = passwordEncoder.encode(password);
        var maybeNewUser = User.create(firstName, lastName, email, encodedPassword);
        if (maybeNewUser.isFailure()) {
            return Result.failure(maybeNewUser.getError());
        }
        var newUser = maybeNewUser.getValue();
        qrProvider.generate(newUser.getQRFileName(), newUser.getEmail());
        userRepository.save(newUser);
        return Result.success(newUser);
    }
}
