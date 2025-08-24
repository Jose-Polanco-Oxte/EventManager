package jpolanco.applicationcore.user.infrastructure.adapters.output.mysql;

import jpolanco.applicationcore.user.domain.model.User;
import jpolanco.applicationcore.user.domain.repositories.UserRepository;
import jpolanco.applicationcore.user.infrastructure.adapters.mappers.UserMapper;
import jpolanco.applicationcore.user.infrastructure.adapters.output.repositories.JpaRoleRepository;
import jpolanco.applicationcore.user.infrastructure.adapters.output.repositories.JpaUserRepository;
import jpolanco.applicationcore.user.infrastructure.exceptions.UserDataConflictException;
import jpolanco.domainmodel.user.RoleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRepositoryMySQL implements UserRepository {
    private final JpaUserRepository jpaUserRepository;
    private final JpaRoleRepository jpaRoleRepository;

    @Override
    public Optional<User> findById(UUID uuid) {
        return jpaUserRepository.findByUuidAndDeletedFalse(uuid)
                .map(UserMapper::fromPersistence);
    }

    @Override
    public boolean existsById(UUID uuid) {
        return jpaUserRepository.existsByUuid(uuid);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmailAndDeletedFalse(email)
                .map(UserMapper::fromPersistence);
    }

    @Override
    public void softDelete(Long id) {
        jpaUserRepository.softDeleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmailAndDeletedFalse(email);
    }

    @Override
    public User save(User user) {
        Set<RoleEntity> roles = user.getRoles().get().stream()
                .map(r -> jpaRoleRepository.findByName(r)
                        .orElseThrow(() -> new UserDataConflictException("Role not found: " + r)))
                .collect(Collectors.toSet());

        var userEntity = UserMapper.fromDomain(user, roles);
        var domain = jpaUserRepository.save(userEntity);
        return UserMapper.fromPersistence(domain).replaceEventsFrom(user);
    }

    @Override
    public void deleteById(UUID id) {
        jpaUserRepository.deleteByUuid(id);
    }
}
