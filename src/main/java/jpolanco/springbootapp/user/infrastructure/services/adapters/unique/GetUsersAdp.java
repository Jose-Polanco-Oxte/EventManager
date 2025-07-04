package jpolanco.springbootapp.user.infrastructure.services.adapters.unique;

import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.default_services.unique.GetUsersDefault;
import org.springframework.stereotype.Service;

@Service
public class GetUsersA extends GetUsersDefault {
    public GetUsersA(UserQueryRepository queryRepository) {
        super(queryRepository);
    }
}
