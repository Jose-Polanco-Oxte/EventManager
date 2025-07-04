package jpolanco.springbootapp.user.infrastructure.services.adapters.base;

import jpolanco.springbootapp.user.application.ports.input.EncoderProvider;
import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.defaultservices.base.CreateUserDefault;
import org.springframework.stereotype.Service;

@Service
public class CreateUserAdp extends CreateUserDefault {
    public CreateUserAdp(UserQueryRepository queryRepository, UserCommandRepository commandRepository, EncoderProvider passwordEncoder) {
        super(queryRepository, commandRepository, passwordEncoder);
    }
}
