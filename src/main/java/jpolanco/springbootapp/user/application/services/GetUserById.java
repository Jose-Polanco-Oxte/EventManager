package jpolanco.springbootapp.user.application.services;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.application.errors.UserAppError;
import jpolanco.springbootapp.user.application.ports.output.UserRepository;
import jpolanco.springbootapp.user.application.uc.GetUserByIdUC;
import jpolanco.springbootapp.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetUserById implements GetUserByIdUC {
    private final UserRepository repository;

    @Override
    public Result<User> get(String userId) {
        var result = repository.findById(userId);
        return result.map(Result::success)
                .orElseGet(() -> Result.failure(UserAppError.USER_NOT_FOUND));
    }
}
