package jpolanco.springbootapp.user.infrastructure.services.adapters.unique.search;

import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.default_services.unique.search.SearchUserByEmailDefault;
import org.springframework.stereotype.Service;

@Service
public class SearchUserByEmailA extends SearchUserByEmailDefault {
    public SearchUserByEmailA(UserQueryRepository queryRepository) {
        super(queryRepository);
    }
}
