package jpolanco.springbootapp.user.infrastructure.services.adapters.unique;

import jpolanco.springbootapp.user.application.ports.input.AuthenticatorProvider;
import jpolanco.springbootapp.user.application.ports.input.EncoderProvider;
import jpolanco.springbootapp.user.application.ports.output.JwtQueryRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.default_services.unique.LoginDefault;
import org.springframework.stereotype.Service;

@Service
public class LoginA extends LoginDefault {
    public LoginA(UserQueryRepository queryRepository, JwtQueryRepository jwtQueryRepository, EncoderProvider passwordEncoder, AuthenticatorProvider authentication, GenerateJwtA generateJwtUC) {
        super(queryRepository, jwtQueryRepository, passwordEncoder, authentication, generateJwtUC);
    }
}
