package jpolanco.springbootapp.user.infrastructure.services.adapters.derived;

import jpolanco.springbootapp.user.application.ports.input.QRProvider;
import jpolanco.springbootapp.user.application.defaultservices.derived.RegisterAccountDefault;
import jpolanco.springbootapp.user.infrastructure.services.adapters.base.CreateUserAdp;
import jpolanco.springbootapp.user.infrastructure.services.adapters.unique.GenerateTokenAdp;
import org.springframework.stereotype.Service;

@Service
public class RegisterAccountAdp extends RegisterAccountDefault {
    public RegisterAccountAdp(CreateUserAdp createUserAdp, GenerateTokenAdp generateJwtUC, QRProvider qrProvider) {
        super(createUserAdp, generateJwtUC, qrProvider);
    }
}