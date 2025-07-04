package jpolanco.springbootapp.user.infrastructure.services.adapters.derived;

import jpolanco.springbootapp.user.application.ports.input.EncoderProvider;
import jpolanco.springbootapp.user.application.ports.output.JwtCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.defaultservices.derived.UpdateAccountPasswordDefault;
import org.springframework.stereotype.Service;

@Service
public class UpdateAccountPasswordAdp extends UpdateAccountPasswordDefault {
    public UpdateAccountPasswordAdp(UserQueryRepository queryRepository, UserCommandRepository commandRepository, JwtCommandRepository jwtCommandRepository, EncoderProvider passwordEncoder) {
        super(queryRepository, commandRepository, jwtCommandRepository, passwordEncoder);
    }
}
