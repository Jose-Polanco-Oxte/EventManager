package jpolanco.springbootapp.user.infrastructure.services.adapters.unique.search;

import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.default_services.unique.search.SearchUserByNameDefault;
import org.springframework.stereotype.Service;

@Service
public class SearchUserByNameA extends SearchUserByNameDefault {
    public SearchUserByNameA(UserQueryRepository queryRepository) {
        super(queryRepository);
    }
}
