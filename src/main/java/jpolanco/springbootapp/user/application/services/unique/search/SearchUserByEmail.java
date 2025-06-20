package jpolanco.springbootapp.user.application.services.unique.search;

import jpolanco.springbootapp.user.application.uc.unique.search.SearchUserByEmailUC;
import jpolanco.springbootapp.user.application.ports.output.UserRepository;
import jpolanco.springbootapp.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchUserByEmail implements SearchUserByEmailUC {
    private final UserRepository userRepository;
    @Override
    public List<User> search(String email, int numberOfResults) {
        return userRepository.searchByEmail(email, numberOfResults);
    }
}
