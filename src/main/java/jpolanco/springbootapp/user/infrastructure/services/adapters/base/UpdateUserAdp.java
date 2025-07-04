package jpolanco.springbootapp.user.infrastructure.services.adapters.base;

import jpolanco.springbootapp.user.application.ports.input.EncoderProvider;
import jpolanco.springbootapp.user.application.ports.input.QRProvider;
import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.defaultservices.base.UpdateUserDefault;
import org.springframework.stereotype.Service;

@Service
public class UpdateUserAdp extends UpdateUserDefault {
    public UpdateUserAdp(UserQueryRepository queryRepository, UserCommandRepository commandRepository, EncoderProvider passwordEncoder, QRProvider qrProvider) {
        super(queryRepository, commandRepository, passwordEncoder, qrProvider);
    }
}
