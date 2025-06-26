package jpolanco.springbootapp.user.application.utils;

import jpolanco.springbootapp.shared.application.AppError;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidation {
    private final UserQueryRepository queryRepository;

    private boolean emailExist(String email) {
        return queryRepository.findByEmail(email).isPresent();
    }

    public Result<Void> onCreateUserIsValid(String email) {
        if (emailExist(email)) {
            return Result.failure(AppError.CONFLICT);
        }
        return Result.success();
    }
}
