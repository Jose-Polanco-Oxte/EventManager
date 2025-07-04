package jpolanco.springbootapp.user.infrastructure.services.adapters.unique.search;

import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.defaultservices.unique.search.SearchUserByEmailDefault;
import org.springframework.stereotype.Service;

@Service
public class SearchUserByEmailAdp extends SearchUserByEmailDefault {
    public SearchUserByEmailAdp(UserQueryRepository queryRepository) {
        super(queryRepository);
    }
}
