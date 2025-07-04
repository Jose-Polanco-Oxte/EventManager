package jpolanco.springbootapp.user.infrastructure.services.adapters.unique;

import jpolanco.springbootapp.user.application.ports.input.JwtProvider;
import jpolanco.springbootapp.user.application.ports.output.JwtCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.defaultservices.unique.RefreshTokenDefault;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenAdp extends RefreshTokenDefault {
    public RefreshTokenAdp(UserQueryRepository userQueryRepository, JwtCommandRepository jwtCommandRepository, GenerateTokenAdp generateJwtUC, JwtProvider jwtProvider) {
        super(userQueryRepository, jwtCommandRepository, generateJwtUC, jwtProvider);
    }
}
