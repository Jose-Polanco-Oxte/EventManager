package jpolanco.springbootapp.user.infrastructure.services.adapters.derived;

import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.defaultservices.derived.UpdateAccountNameDefault;
import org.springframework.stereotype.Service;

@Service
public class UpdateAccountNameAdp extends UpdateAccountNameDefault {
    public UpdateAccountNameAdp(UserQueryRepository queryRepository, UserCommandRepository commandRepository) {
        super(queryRepository, commandRepository);
    }
}