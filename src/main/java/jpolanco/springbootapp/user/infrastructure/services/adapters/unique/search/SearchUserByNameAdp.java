package jpolanco.springbootapp.user.infrastructure.services.adapters.unique.search;

import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.defaultservices.unique.search.SearchUserByNameDefault;
import org.springframework.stereotype.Service;

@Service
public class SearchUserByNameAdp extends SearchUserByNameDefault {
    public SearchUserByNameAdp(UserQueryRepository queryRepository) {
        super(queryRepository);
    }
}
