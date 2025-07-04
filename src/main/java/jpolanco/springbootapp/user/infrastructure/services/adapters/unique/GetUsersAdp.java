package jpolanco.springbootapp.user.infrastructure.services.adapters.unique;

import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.defaultservices.unique.GetUsersDefault;
import org.springframework.stereotype.Service;

@Service
public class GetUsersAdp extends GetUsersDefault {
    public GetUsersAdp(UserQueryRepository queryRepository) {
        super(queryRepository);
    }
}
