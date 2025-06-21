package jpolanco.springbootapp.user.application.services.unique;

import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.uc.unique.GetUserByIdUC;
import jpolanco.springbootapp.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetUserById implements GetUserByIdUC {
    private final UserQueryRepository queryRepository;

    @Override
    public Optional<User> get(String userId) {
        return queryRepository.findById(userId);
    }
}
