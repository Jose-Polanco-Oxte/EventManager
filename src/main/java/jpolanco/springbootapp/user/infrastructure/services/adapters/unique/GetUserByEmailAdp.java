package jpolanco.springbootapp.user.infrastructure.services.adapters.unique;

import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.defaultservices.unique.GetUserByEmailDefault;
import org.springframework.stereotype.Service;

@Service
public class GetUserByEmailAdp extends GetUserByEmailDefault {
    public GetUserByEmailAdp(UserQueryRepository queryRepository) {
        super(queryRepository);
    }
}
