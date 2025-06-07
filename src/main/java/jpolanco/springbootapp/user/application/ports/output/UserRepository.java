package jpolanco.springbootapp.user.application.ports.output;

import jpolanco.springbootapp.shared.domain.CRUDRepository;
import jpolanco.springbootapp.user.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CRUDRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findById(String userId);

    User save(User user);

    void deleteById(String userId);

    List<User> findAll();
}