package jpolanco.springbootapp.user.application.ports.output;

import jpolanco.springbootapp.shared.application.adapters.output.PageableRepository;
import jpolanco.springbootapp.user.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserQueryRepository extends PageableRepository<User, String> {
    Optional<User> findById(String id);
    Optional<User> findByEmail(String email);
    List<User> searchByName(String name, int size);
    List<User> searchByEmail(String email, int size);
}