package jpolanco.springbootapp.user.infrastructure.services.adapters.derived;

import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.default_services.derived.DeactivateAccountDefault;
import jpolanco.springbootapp.user.infrastructure.services.adapters.base.DeactivateUserAdp;
import org.springframework.stereotype.Service;

@Service
public class DeactivateAccountA extends DeactivateAccountDefault {
    public DeactivateAccountA(UserQueryRepository queryRepository, DeactivateUserAdp deactivateUserUC) {
        super(queryRepository, deactivateUserUC);
    }
}
