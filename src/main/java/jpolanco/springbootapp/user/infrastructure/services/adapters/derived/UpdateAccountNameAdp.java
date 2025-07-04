package jpolanco.springbootapp.user.infrastructure.services.adapters.derived;

import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.default_services.derived.UpdateAccountNameDefault;
import org.springframework.stereotype.Service;

@Service
public class UpdateProfileNameAdp extends UpdateAccountNameDefault {
    public UpdateProfileNameAdp(UserQueryRepository queryRepository, UserCommandRepository commandRepository) {
        super(queryRepository, commandRepository);
    }
}