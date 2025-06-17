package jpolanco.springbootapp.user.application.services;
import jpolanco.springbootapp.user.application.ports.output.UserRepository;
import jpolanco.springbootapp.user.application.uc.GetAllUsersUC;
import jpolanco.springbootapp.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GetAllUsers implements GetAllUsersUC {

    private final UserRepository userRepository;

    @Override
    public List<User> get() {
        return userRepository.findAll();
    }
}