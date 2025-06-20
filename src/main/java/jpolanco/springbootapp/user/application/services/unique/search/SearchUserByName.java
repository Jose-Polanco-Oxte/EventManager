package jpolanco.springbootapp.user.application.services.unique.search;

import jpolanco.springbootapp.user.application.ports.output.UserRepository;
import jpolanco.springbootapp.user.application.uc.unique.search.SearchUserByNameUC;
import jpolanco.springbootapp.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchUserByName implements SearchUserByNameUC {
    private final UserRepository userRepository;

    @Override
    public List<User> search(String name, int numberOfResults) {
        return userRepository.searchByName(name, numberOfResults);
    }
}
