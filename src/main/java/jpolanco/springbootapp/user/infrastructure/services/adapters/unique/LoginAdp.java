package jpolanco.springbootapp.user.infrastructure.services.adapters.unique;

import jpolanco.springbootapp.user.application.ports.input.AuthenticatorProvider;
import jpolanco.springbootapp.user.application.ports.input.EncoderProvider;
import jpolanco.springbootapp.user.application.ports.output.JwtQueryRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.defaultservices.unique.LoginDefault;
import org.springframework.stereotype.Service;

@Service
public class LoginAdp extends LoginDefault {
    public LoginAdp(UserQueryRepository queryRepository, JwtQueryRepository jwtQueryRepository, EncoderProvider passwordEncoder, AuthenticatorProvider authentication, GenerateTokenAdp generateJwtUC) {
        super(queryRepository, jwtQueryRepository, passwordEncoder, authentication, generateJwtUC);
    }
}
