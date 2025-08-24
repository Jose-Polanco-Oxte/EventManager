package jpolanco.applicationcore.user.infrastructure.adapters.output.repositories;

import jpolanco.domainmodel.user.RoleEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface JpaRoleRepository extends CrudRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(String name);

    /**
     * Check if a role with the given name does not exist in the database.
     *
     * @param name the name of the role to check
     * @return true if the role does not exist, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(r) = 0 THEN true ELSE false END FROM RoleEntity r WHERE r.name = ?1")
    boolean notExistByName(String name);
}