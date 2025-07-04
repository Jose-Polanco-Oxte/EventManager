package jpolanco.springbootapp.user.infrastructure.services.adapters.unique;

import jpolanco.springbootapp.user.application.ports.input.JwtProvider;
import jpolanco.springbootapp.user.application.ports.output.JwtCommandRepository;
import jpolanco.springbootapp.user.application.default_services.unique.GenerateTokenDefault;
import org.springframework.stereotype.Service;

@Service
public class GenerateJwtA extends GenerateTokenDefault {
    public GenerateJwtA(JwtCommandRepository commandRepository, JwtProvider jwtProvider) {
        super(commandRepository, jwtProvider);
    }
}