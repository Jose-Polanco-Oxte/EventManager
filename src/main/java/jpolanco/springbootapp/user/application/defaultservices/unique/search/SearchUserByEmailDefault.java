package jpolanco.springbootapp.user.application.defaultservices.unique.search;

import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.usecase.unique.search.SearchUserByEmail;
import jpolanco.springbootapp.user.domain.model.valueobjects.User;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class SearchUserByEmailDefault implements SearchUserByEmail {
    private final UserQueryRepository queryRepository;

    @Override
    public List<User> search(String email, int numberOfResults) {
        return queryRepository.searchByEmail(email, numberOfResults);
    }
}
