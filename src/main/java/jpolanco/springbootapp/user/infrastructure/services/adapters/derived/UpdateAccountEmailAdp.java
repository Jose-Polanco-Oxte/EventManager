package jpolanco.springbootapp.user.infrastructure.services.adapters.derived;

import jpolanco.springbootapp.user.application.ports.output.JwtCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.defaultservices.derived.UpdateAccountEmailDefault;
import org.springframework.stereotype.Service;

@Service
public class UpdateAccountEmailAdp extends UpdateAccountEmailDefault {
    public UpdateAccountEmailAdp(UserQueryRepository queryRepository, UserCommandRepository commandRepository, JwtCommandRepository jwtCommandRepository) {
        super(queryRepository, commandRepository, jwtCommandRepository);
    }
}