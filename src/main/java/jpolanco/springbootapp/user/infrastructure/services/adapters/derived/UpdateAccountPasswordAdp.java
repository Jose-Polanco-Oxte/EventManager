package jpolanco.springbootapp.user.infrastructure.services.adapters.derived;

import jpolanco.springbootapp.user.application.ports.input.EncoderProvider;
import jpolanco.springbootapp.user.application.ports.output.JwtCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.default_services.derived.UpdateAccountPasswordDefault;
import org.springframework.stereotype.Service;

@Service
public class UpdateProfilePasswordAdp extends UpdateAccountPasswordDefault {
    public UpdateProfilePasswordAdp(UserQueryRepository queryRepository, UserCommandRepository commandRepository, JwtCommandRepository jwtCommandRepository, EncoderProvider passwordEncoder) {
        super(queryRepository, commandRepository, jwtCommandRepository, passwordEncoder);
    }
}
