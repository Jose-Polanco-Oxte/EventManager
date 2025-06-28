package jpolanco.springbootapp.user.application.services.unique;

import jpolanco.springbootapp.shared.application.AppError;
import jpolanco.springbootapp.shared.domain.utils.Error;
import jpolanco.springbootapp.shared.utils.Either;
import jpolanco.springbootapp.user.application.ports.input.EncoderProvider;
import jpolanco.springbootapp.user.application.ports.input.QRProvider;
import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.uc.unique.CreateUserUC;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class CreateUser implements CreateUserUC {
    private final UserQueryRepository queryRepository;
    private final UserCommandRepository commandRepository;
    private final EncoderProvider passwordEncoder;
    private final QRProvider qrProvider;

    @Override
    public Either<User, List<Error>> create(RegisterRequest request) {
        if (queryRepository.findByEmail(request.email()).isPresent()) {
            return Either.right(List.of(AppError.CONFLICT
                    .withField("Email")
                    .withMessage("Email already exists: " + request.email()).convertToError()));
        }
        var encodedPassword = passwordEncoder.encode(request.password());
        var either = User.create(
                request.firstName(),
                request.lastName(),
                request.email(),
                encodedPassword
        );
        if (either.isRight()) {
            return Either.right(either.getRight());
        }
        var newUser = either.getLeft();
        qrProvider.generate(newUser.getQRFileName(), newUser.getEmail());
        var savedUser = commandRepository.save(newUser);
        return Either.left(savedUser);
    }
}
