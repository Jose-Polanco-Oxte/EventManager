package jpolanco.springbootapp.user.infrastructure.services.adapters.base;

import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.default_services.base.ReactivateUserDefault;
import org.springframework.stereotype.Service;

@Service
public class ReactivateUserA extends ReactivateUserDefault {
    public ReactivateUserA(UserQueryRepository queryRepository, UserCommandRepository commandRepository) {
        super(queryRepository, commandRepository);
    }
}
