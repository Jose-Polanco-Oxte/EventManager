package jpolanco.springbootapp.user.infrastructure.services.adapters.derived;

import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.default_services.derived.ReactivateAccountDefault;
import jpolanco.springbootapp.user.infrastructure.services.adapters.base.ReactivateUserAdp;
import org.springframework.stereotype.Service;

@Service
public class ReactivateAccountA extends ReactivateAccountDefault {
    public ReactivateAccountA(UserQueryRepository queryRepository, ReactivateUserAdp reactivateUserUC) {
        super(queryRepository, reactivateUserUC);
    }
}
