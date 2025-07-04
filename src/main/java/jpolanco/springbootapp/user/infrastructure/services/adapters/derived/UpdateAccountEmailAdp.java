package jpolanco.springbootapp.user.infrastructure.services.adapters.derived;

import jpolanco.springbootapp.user.application.ports.output.JwtCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.default_services.derived.UpdateAccountEmailDefault;
import org.springframework.stereotype.Service;

@Service
public class UpdateAccountEmailA extends UpdateAccountEmailDefault {
    public UpdateAccountEmailA(UserQueryRepository queryRepository, UserCommandRepository commandRepository, JwtCommandRepository jwtCommandRepository) {
        super(queryRepository, commandRepository, jwtCommandRepository);
    }
}