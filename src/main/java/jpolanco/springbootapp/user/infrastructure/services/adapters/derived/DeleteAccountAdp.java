package jpolanco.springbootapp.user.infrastructure.services.adapters.derived;

import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.default_services.derived.DeleteAccountDefault;
import jpolanco.springbootapp.user.infrastructure.services.adapters.base.DeleteUserAdp;
import org.springframework.stereotype.Service;

@Service
public class DeleteAccountA extends DeleteAccountDefault {
    public DeleteAccountA(UserQueryRepository queryRepository, DeleteUserAdp deleteUserUC) {
        super(queryRepository, deleteUserUC);
    }
}
