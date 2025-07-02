package jpolanco.springbootapp.user.infrastructure.adapters.output.mysql;

import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.domain.model.value_objects.User;
import jpolanco.springbootapp.user.infrastructure.adapters.mappers.entity.UserEntityMapper;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.RoleEntity;
import jpolanco.springbootapp.user.infrastructure.adapters.output.repository.JpaRoleRepository;
import jpolanco.springbootapp.user.infrastructure.adapters.output.repository.JpaUserRepository;
import jpolanco.springbootapp.user.infrastructure.errors.UserIntegrity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserCommandMySQL implements UserCommandRepository {
    private final JpaUserRepository jpaUserRepository;
    private final JpaRoleRepository jpaRoleRepository;
    private final UserEntityMapper mapper;

    @Override
    public User save(User user) {
        Set<RoleEntity> roles = user.getRoles().stream()
                .map(r -> jpaRoleRepository.findByName(r)
                            .orElseThrow(() -> new UserIntegrity("Role not found: " + r)))
                .collect(Collectors.toSet());

        var userEntity = mapper.fromDomain(user, roles);
        var savedEntity = jpaUserRepository.save(userEntity);
        return mapper.fromPersistence(savedEntity).replaceEventsFrom(user);
    }

    @Override
    public void deleteById(Long userId) {
        jpaUserRepository.deleteById(userId);
    }
}
