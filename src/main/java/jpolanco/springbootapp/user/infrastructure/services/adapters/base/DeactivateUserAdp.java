package jpolanco.springbootapp.user.infrastructure.services.adapters.base;

import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.default_services.base.DeactivateUserDefault;
import org.springframework.stereotype.Service;

@Service
public class DeactivateUserA extends DeactivateUserDefault {
    public DeactivateUserA(UserQueryRepository queryRepository, UserCommandRepository commandRepository) {
        super(queryRepository, commandRepository);
    }
}
