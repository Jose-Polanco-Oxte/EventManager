package jpolanco.springbootapp.user.infrastructure.adapters.output.repository;

import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaRoleRepository extends JpaRepository<RoleEntity, Long> {
    boolean existsByName(String name);

    Optional<RoleEntity> findByName(String name);
}
