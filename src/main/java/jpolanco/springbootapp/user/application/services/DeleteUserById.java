package jpolanco.springbootapp.user.application.services;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.application.ports.input.QRProvider;
import jpolanco.springbootapp.user.application.ports.output.UserRepository;
import jpolanco.springbootapp.user.application.utils.UserValidation;
import jpolanco.springbootapp.user.application.uc.DeleteUserByIdUC;
import jpolanco.springbootapp.user.infrastructure.adapters.output.repository.JpaTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class DeleteUserById implements DeleteUserByIdUC {
    private final UserRepository userRepository;
    private final UserValidation userValidation;
    private final JpaTokenRepository jpaTokenRepository;
    private final QRProvider qrProvider;

    @Override
    public Result<Void> delete(String userId) {
        var validUser = userValidation.basicValid(userId);
        if (validUser.isFailure()) {
            return Result.failure(validUser.getError());
        }
        var user = validUser.getValue();
        // Check if the user has any tokens associated with them
        jpaTokenRepository.deleteAllByUserId(UUID.fromString(userId));
        qrProvider.delete(user.getQRFileName());
        // Delete the user
        userRepository.deleteById(userId);
        return Result.success();
    }
}
