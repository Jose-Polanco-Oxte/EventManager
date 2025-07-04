package jpolanco.springbootapp.user.application.defaultservices.unique.search;

import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.usecase.unique.search.SearchUserByName;
import jpolanco.springbootapp.user.domain.model.valueobjects.User;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class SearchUserByNameDefault implements SearchUserByName {
    private final UserQueryRepository queryRepository;

    @Override
    public List<User> search(String name, int numberOfResults) {
        return queryRepository.searchByName(name, numberOfResults);
    }
}
