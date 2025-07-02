package jpolanco.springbootapp.user.application.ports.output;

import jpolanco.springbootapp.shared.application.adapters.output.PageableRepository;
import jpolanco.springbootapp.user.domain.model.value_objects.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserQueryRepository extends PageableRepository<User, UUID> {
    Optional<User> findById(Long id);
    Optional<User> findByUuid(UUID uuid);
    Optional<User> findByEmail(String email);

    List<User> searchByName(String name, int size);
    List<User> searchByEmail(String email, int size);
}