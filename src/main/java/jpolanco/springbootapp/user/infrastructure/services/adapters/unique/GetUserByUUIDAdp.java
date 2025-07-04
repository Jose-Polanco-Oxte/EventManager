package jpolanco.springbootapp.user.infrastructure.services.adapters.unique;

import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.default_services.unique.GetUserByUUIDDefault;
import org.springframework.stereotype.Service;

@Service
public class GetUserByIdA extends GetUserByUUIDDefault {
    public GetUserByIdA(UserQueryRepository queryRepository) {
        super(queryRepository);
    }
}
