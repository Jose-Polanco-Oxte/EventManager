package jpolanco.springbootapp.user.infrastructure.services.implementations;

import jpolanco.springbootapp.user.application.usecase.unique.search.SearchUserByEmail;
import jpolanco.springbootapp.user.application.usecase.unique.search.SearchUserByName;
import jpolanco.springbootapp.user.domain.model.valueobjects.User;
import jpolanco.springbootapp.user.infrastructure.services.interfaces.SearchUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchUserServiceImpl implements SearchUserService {
    private final SearchUserByEmail searchUserByEmail;
    private final SearchUserByName searchUserByName;

    @Override
    public List<User> searchByName(String name, int size) {
        return searchUserByEmail.search(name, size);
    }

    @Override
    public List<User> searchByEmail(String email, int size) {
        return searchUserByName.search(email, size);
    }
}
