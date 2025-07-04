package jpolanco.springbootapp.user.application.default_services.unique.search;

import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.use_case.unique.search.SearchUserByNameUC;
import jpolanco.springbootapp.user.domain.model.value_objects.User;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class SearchUserByName implements SearchUserByNameUC {
    private final UserQueryRepository queryRepository;

    @Override
    public List<User> search(String name, int numberOfResults) {
        return queryRepository.searchByName(name, numberOfResults);
    }
}
