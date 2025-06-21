package jpolanco.springbootapp.user.application.services.unique;

import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.uc.unique.GetUserByEmailUC;
import jpolanco.springbootapp.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetUserByEmail implements GetUserByEmailUC {
    private final UserQueryRepository queryRepository;

    @Override
    public Optional<User> get(String email) {
        return queryRepository.findByEmail(email);
    }
}
