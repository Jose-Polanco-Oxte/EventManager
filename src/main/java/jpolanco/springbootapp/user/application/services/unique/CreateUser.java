package jpolanco.springbootapp.user.application.services.unique;

import jpolanco.springbootapp.shared.application.AppError;
import jpolanco.springbootapp.shared.domain.Report;
import jpolanco.springbootapp.shared.utils.SuperResult;
import jpolanco.springbootapp.user.application.ports.input.EncoderProvider;
import jpolanco.springbootapp.user.application.ports.input.QRProvider;
import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.uc.unique.CreateUserUC;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class CreateUser implements CreateUserUC {
    private final UserQueryRepository queryRepository;
    private final UserCommandRepository commandRepository;
    private final EncoderProvider passwordEncoder;
    private final QRProvider qrProvider;

    @Override
    public SuperResult<User, Report> create(RegisterRequest request) {
        if (queryRepository.findByEmail(request.email()).isPresent()) {
            return SuperResult.failure(Report.failure(AppError.CONFLICT
                    .withField("Email")
                    .withMessage("Email already exists: " + request.email())));
        }
        var encodedPassword = passwordEncoder.encode(request.password());
        var result = User.create(
                request.firstName(),
                request.lastName(),
                request.email(),
                encodedPassword
        );
        if (result.isFailure()) {
            return SuperResult.failure(result.getFailure());
        }
        var newUser = result.getSuccess();
        qrProvider.generate(newUser.getQRFileName(), newUser.getEmail());
        var savedUser = commandRepository.save(newUser);
        return SuperResult.success(savedUser);
    }
}
