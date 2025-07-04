package jpolanco.springbootapp.user.infrastructure.services.adapters.derived;

import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.defaultservices.derived.DeleteAccountDefault;
import jpolanco.springbootapp.user.infrastructure.services.adapters.base.DeleteUserAdp;
import org.springframework.stereotype.Service;

@Service
public class DeleteAccountAdp extends DeleteAccountDefault {
    public DeleteAccountAdp(UserQueryRepository queryRepository, DeleteUserAdp deleteUserUC) {
        super(queryRepository, deleteUserUC);
    }
}
