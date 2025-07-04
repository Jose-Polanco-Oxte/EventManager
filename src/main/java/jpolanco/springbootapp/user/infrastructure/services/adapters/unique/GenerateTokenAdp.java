package jpolanco.springbootapp.user.infrastructure.services.adapters.unique;

import jpolanco.springbootapp.user.application.ports.input.JwtProvider;
import jpolanco.springbootapp.user.application.ports.output.JwtCommandRepository;
import jpolanco.springbootapp.user.application.defaultservices.unique.GenerateTokenDefault;
import org.springframework.stereotype.Service;

@Service
public class GenerateTokenAdp extends GenerateTokenDefault {
    public GenerateTokenAdp(JwtCommandRepository commandRepository, JwtProvider jwtProvider) {
        super(commandRepository, jwtProvider);
    }
}