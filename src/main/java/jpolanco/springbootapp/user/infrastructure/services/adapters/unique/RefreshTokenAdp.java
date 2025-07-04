package jpolanco.springbootapp.user.infrastructure.services.adapters.unique;

import jpolanco.springbootapp.user.application.ports.input.JwtProvider;
import jpolanco.springbootapp.user.application.ports.output.JwtCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.default_services.unique.RefreshTokenDefault;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenA extends RefreshTokenDefault {
    public RefreshTokenA(UserQueryRepository userQueryRepository, JwtCommandRepository jwtCommandRepository, GenerateJwtA generateJwtUC, JwtProvider jwtProvider) {
        super(userQueryRepository, jwtCommandRepository, generateJwtUC, jwtProvider);
    }
}
