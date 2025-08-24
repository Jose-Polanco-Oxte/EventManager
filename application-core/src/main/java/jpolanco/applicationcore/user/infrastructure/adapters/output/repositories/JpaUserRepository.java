package jpolanco.applicationcore.user.infrastructure.adapters.output.repositories;

import jpolanco.domainmodel.user.UserEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaUserRepository extends CrudRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

    Optional<UserEntity> findByUuid(UUID uuid);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUuidAndDeletedFalse(UUID uuid);

    Optional<UserEntity> findByEmailAndDeletedFalse(String email);

    void deleteByUuid(UUID uuid);

    boolean existsByUuid(UUID uuid);

    boolean existsByEmailAndDeletedFalse(String email);

    /**
     * Update the 'deleted' field to true for the user with the specified id.
     *
     * @param id the id of the user to be soft deleted
     */
    @Modifying
    @Query("UPDATE UserEntity u SET u.deleted = true WHERE u.id = ?1")
    void softDeleteById(Long id);

    Optional<Long> findIdByUuid(UUID uuid);
}