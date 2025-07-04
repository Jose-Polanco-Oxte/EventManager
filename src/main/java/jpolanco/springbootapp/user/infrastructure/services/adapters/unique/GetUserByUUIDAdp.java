package jpolanco.springbootapp.user.infrastructure.services.adapters.unique;

import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.defaultservices.unique.GetUserByUUIDDefault;
import org.springframework.stereotype.Service;

@Service
public class GetUserByUUIDAdp extends GetUserByUUIDDefault {
    public GetUserByUUIDAdp(UserQueryRepository queryRepository) {
        super(queryRepository);
    }
}
