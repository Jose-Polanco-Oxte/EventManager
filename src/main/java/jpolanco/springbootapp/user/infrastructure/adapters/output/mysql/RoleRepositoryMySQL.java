package jpolanco.springbootapp.user.infrastructure.adapters.output.mysql;

import jpolanco.springbootapp.user.application.ports.output.RolesRepository;
import jpolanco.springbootapp.user.infrastructure.errors.UserIntegrity;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.RoleEntity;
import jpolanco.springbootapp.user.infrastructure.adapters.output.repository.JpaRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryMySQL implements RolesRepository {

    private final JpaRoleRepository jpaRoleRepository;

    @Override
    public boolean existsByName(String name) {
        return jpaRoleRepository.existsByName(name);
    }

    @Override
    public void save(String name) {
        jpaRoleRepository.save(new RoleEntity(name));
    }
}
