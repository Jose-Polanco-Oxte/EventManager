package jpolanco.springbootapp.user.application.services.unique;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.application.ports.input.EncoderProvider;
import jpolanco.springbootapp.user.application.ports.input.QRProvider;
import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.uc.unique.CreateUserUC;
import jpolanco.springbootapp.user.application.utils.UserValidation;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUser implements CreateUserUC {
    private final UserCommandRepository commandRepository;
    private final EncoderProvider passwordEncoder;
    private final QRProvider qrProvider;
    private final UserValidation userValidation;

    @Override
    public Result<User> create(RegisterRequest request) {
        var valid = userValidation.onCreateUserIsValid(request.email());
        if (valid.isFailure()) {
            return Result.failure(valid.getError());
        }
        var encodedPassword = passwordEncoder.encode(request.password());
        var maybeNewUser = User.create(
                request.firstName(),
                request.lastName(),
                request.email(),
                encodedPassword
        );
        if (maybeNewUser.isFailure()) {
            return Result.failure(maybeNewUser.getError());
        }
        var newUser = maybeNewUser.getValue();
        qrProvider.generate(newUser.getQRFileName(), newUser.getEmail());
        commandRepository.save(newUser);
        return Result.success(newUser);
    }
}
