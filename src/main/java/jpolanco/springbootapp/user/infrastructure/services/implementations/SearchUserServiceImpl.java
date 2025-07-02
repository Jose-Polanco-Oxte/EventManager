package jpolanco.springbootapp.user.infrastructure.services.implementations;

import jpolanco.springbootapp.user.application.uc.unique.search.SearchUserByEmailUC;
import jpolanco.springbootapp.user.application.uc.unique.search.SearchUserByNameUC;
import jpolanco.springbootapp.user.domain.model.value_objects.User;
import jpolanco.springbootapp.user.infrastructure.services.interfaces.SearchUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchUserServiceImpl implements SearchUserService {
    private final SearchUserByEmailUC searchUserByEmailUC;
    private final SearchUserByNameUC searchUserByNameUC;

    @Override
    public List<User> searchByName(String name, int size) {
        return searchUserByEmailUC.search(name, size);
    }

    @Override
    public List<User> searchByEmail(String email, int size) {
        return searchUserByNameUC.search(email, size);
    }
}
