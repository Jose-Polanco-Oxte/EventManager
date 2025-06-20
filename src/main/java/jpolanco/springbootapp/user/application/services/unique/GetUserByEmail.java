package jpolanco.springbootapp.user.application.services.unique;

import jpolanco.springbootapp.user.application.ports.output.UserRepository;
import jpolanco.springbootapp.user.application.uc.unique.GetUserByEmailUC;
import jpolanco.springbootapp.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetUserByEmail implements GetUserByEmailUC {

    private final UserRepository userRepository;

    @Override
    public Optional<User> get(String email) {
        return userRepository.findByEmail(email);
    }
}
