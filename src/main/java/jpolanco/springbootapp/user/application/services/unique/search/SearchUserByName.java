package jpolanco.springbootapp.user.application.services.unique.search;

import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.uc.unique.search.SearchUserByNameUC;
import jpolanco.springbootapp.user.domain.model.value_objects.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchUserByName implements SearchUserByNameUC {
    private final UserQueryRepository queryRepository;

    @Override
    public List<User> search(String name, int numberOfResults) {
        return queryRepository.searchByName(name, numberOfResults);
    }
}
