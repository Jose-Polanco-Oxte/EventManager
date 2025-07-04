package jpolanco.springbootapp.user.infrastructure.services.adapters.base;

import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.defaultservices.base.DeactivateUserDefault;
import org.springframework.stereotype.Service;

@Service
public class DeactivateUserAdp extends DeactivateUserDefault {
    public DeactivateUserAdp(UserQueryRepository queryRepository, UserCommandRepository commandRepository) {
        super(queryRepository, commandRepository);
    }
}
