package jpolanco.springbootapp.user.application.uc.derived;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.model.User;

public interface DeactivateProfileUC {

    Result<User> deactivate(String userId, String reason);
}
