package jpolanco.springbootapp.user.infrastructure.services.adapters.unique;

import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.default_services.unique.GetUserByEmailDefault;
import org.springframework.stereotype.Service;

@Service
public class GetUserByEmailA extends GetUserByEmailDefault {
    public GetUserByEmailA(UserQueryRepository queryRepository) {
        super(queryRepository);
    }
}
