package jpolanco.springbootapp.user.application.services;

import jpolanco.springbootapp.user.application.ports.output.UserRepository;
import jpolanco.springbootapp.user.application.uc.SearchUserByNameUC;
import jpolanco.springbootapp.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchUserByName implements SearchUserByNameUC {

    private final UserRepository userRepository;

    @Override
    public List<User> searchByName(String name, int numberOfResults) {
        return userRepository.searchByName(name, numberOfResults);
    }
}
