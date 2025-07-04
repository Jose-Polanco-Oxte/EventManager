package jpolanco.springbootapp.user.infrastructure.services.adapters.derived;

import jpolanco.springbootapp.user.application.ports.input.QRProvider;
import jpolanco.springbootapp.user.application.default_services.derived.RegisterAccountDefault;
import jpolanco.springbootapp.user.infrastructure.services.adapters.base.CreateUserAdp;
import jpolanco.springbootapp.user.infrastructure.services.adapters.unique.GenerateJwtA;
import org.springframework.stereotype.Service;

@Service
public class RegisterAccountA extends RegisterAccountDefault {
    public RegisterAccountA(CreateUserAdp createUserAdp, GenerateJwtA generateJwtUC, QRProvider qrProvider) {
        super(createUserAdp, generateJwtUC, qrProvider);
    }
}