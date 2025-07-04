package jpolanco.springbootapp.user.infrastructure.services.adapters.base;

import jpolanco.springbootapp.user.application.ports.input.QRProvider;
import jpolanco.springbootapp.user.application.ports.output.JwtCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.default_services.base.DeleteUserDefault;
import org.springframework.stereotype.Service;

@Service
public class DeleteUserA extends DeleteUserDefault {
    public DeleteUserA(UserQueryRepository queryRepository, UserCommandRepository commandRepository, JwtCommandRepository jwtCommandRepository, QRProvider qrProvider) {
        super(queryRepository, commandRepository, jwtCommandRepository, qrProvider);
    }
}
