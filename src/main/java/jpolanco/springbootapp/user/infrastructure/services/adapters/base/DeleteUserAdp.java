package jpolanco.springbootapp.user.infrastructure.services.adapters.base;

import jpolanco.springbootapp.user.application.ports.input.QRProvider;
import jpolanco.springbootapp.user.application.ports.output.JwtCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.defaultservices.base.DeleteUserDefault;
import org.springframework.stereotype.Service;

@Service
public class DeleteUserAdp extends DeleteUserDefault {
    public DeleteUserAdp(UserQueryRepository queryRepository, UserCommandRepository commandRepository, JwtCommandRepository jwtCommandRepository, QRProvider qrProvider) {
        super(queryRepository, commandRepository, jwtCommandRepository, qrProvider);
    }
}
