package jpolanco.springbootapp.user.application.uc.unique;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.model.User;

public interface LoginUC {
    Result<User> login(String email, String password);
}
