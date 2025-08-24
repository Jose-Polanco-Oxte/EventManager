package jpolanco.applicationcore.user.domain.repositories;

import jpolanco.applicationcore.shared.application.adapters.output.BaseRepository;
import jpolanco.applicationcore.user.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends BaseRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    void softDelete(Long id);

    boolean existsByEmail(String email);
}