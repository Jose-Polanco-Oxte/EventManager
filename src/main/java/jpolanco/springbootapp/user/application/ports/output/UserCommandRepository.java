package jpolanco.springbootapp.user.application.ports.output;

import jpolanco.springbootapp.shared.application.adapters.output.CUDRepository;
import jpolanco.springbootapp.user.domain.model.User;

public interface UserCommandRepository extends CUDRepository<User, String> {
}