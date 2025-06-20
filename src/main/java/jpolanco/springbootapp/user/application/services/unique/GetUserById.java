package jpolanco.springbootapp.user.application.services.unique;

import jpolanco.springbootapp.user.application.ports.output.UserRepository;
import jpolanco.springbootapp.user.application.uc.unique.GetUserByIdUC;
import jpolanco.springbootapp.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GetUserById implements GetUserByIdUC {
    private final UserRepository repository;

    @Override
    public Optional<User> get(String userId) {
        return repository.findById(userId);
    }
}
