package jpolanco.springbootapp.user.application.services.base;

import jpolanco.springbootapp.shared.application.AppError;
import jpolanco.springbootapp.shared.domain.Report;
import jpolanco.springbootapp.shared.utils.SuperResult;
import jpolanco.springbootapp.user.application.ports.input.EncoderProvider;
import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.uc.base.CreateUserUC;
import jpolanco.springbootapp.user.domain.model.value_objects.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class CreateUser implements CreateUserUC {
    private final UserQueryRepository queryRepository;
    private final UserCommandRepository commandRepository;
    private final EncoderProvider passwordEncoder;

    @Override
    public SuperResult<User, Report> create(String email, String firstName, String lastName, String password) {
        if (queryRepository.findByEmail(email).isPresent()) {
            return SuperResult.failure(Report.failure(AppError.CONFLICT
                    .withField("Email")
                    .withMessage("Email already exists: " + email)));
        }
        var encodedPassword = passwordEncoder.encode(password);
        var result = User.create(
                firstName,
                lastName,
                email,
                encodedPassword
        );
        if (result.isFailure()) {
            return SuperResult.failure(result.getFailure());
        }
        var newUser = result.getSuccess();
        var savedUser = commandRepository.save(newUser);
        return SuperResult.success(savedUser);
    }
}
