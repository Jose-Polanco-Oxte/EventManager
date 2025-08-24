package jpolanco.applicationcore.user.infrastructure.adapters.output.mysql;

import jpolanco.applicationcore.user.application.ports.output.UserQueryRepository;
import jpolanco.applicationcore.user.domain.model.User;
import jpolanco.applicationcore.user.infrastructure.adapters.mappers.UserMapper;
import jpolanco.applicationcore.user.infrastructure.adapters.output.criteria.SpecBuilder;
import jpolanco.applicationcore.user.infrastructure.adapters.output.criteria.UserSpecs;
import jpolanco.applicationcore.user.infrastructure.adapters.output.repositories.JpaUserRepository;
import jpolanco.domainmodel.user.UserEntity;
import jpolanco.domainmodel.user.UserEntity_;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserQueryMySQL implements UserQueryRepository {
    private final JpaUserRepository jpaUserRepository;

    @Override
    public Optional<User> findById(Long id) {
        return jpaUserRepository.findById(id)
                .map(UserMapper::fromPersistence);
    }

    @Override
    public Optional<User> findByUuid(UUID uuid) {
        return jpaUserRepository.findByUuid(uuid)
                .map(UserMapper::fromPersistence);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email)
                .map(UserMapper::fromPersistence);
    }

    @Override
    public Optional<User> findByIdUserFilter(Long userId) {
        Specification<UserEntity> specs = SpecBuilder.<UserEntity>init()
                .and(UserSpecs.userQueryFilter())
                .eq(UserEntity_.id, userId)
                .build();

        return jpaUserRepository.findOne(specs)
                .map(UserMapper::fromPersistence);
    }

    @Override
    public Optional<User> findByUuidUserFilter(UUID uuid) {
        Specification<UserEntity> specs = SpecBuilder.<UserEntity>init()
                .and(UserSpecs.userQueryFilter())
                .eq(UserEntity_.uuid, uuid)
                .build();

        return jpaUserRepository.findOne(specs)
                .map(UserMapper::fromPersistence);
    }

    @Override
    public Optional<User> findByEmailUserFilter(String email) {
        Specification<UserEntity> specs = SpecBuilder.<UserEntity>init()
                .and(UserSpecs.userQueryFilter())
                .eq(UserEntity_.email, email)
                .build();

        return jpaUserRepository.findOne(specs)
                .map(UserMapper::fromPersistence);
    }

    @Override
    public Optional<User> findByIdAndNotDeleted(Long userId) {
        Specification<UserEntity> specs = SpecBuilder.<UserEntity>init()
                .isFalse(UserEntity_.deleted)
                .eq(UserEntity_.id, userId)
                .build();

        return jpaUserRepository.findOne(specs)
                .map(UserMapper::fromPersistence);
    }

    @Override
    public Optional<User> findByUuidAndNotDeleted(UUID uuid) {
        Specification<UserEntity> specs = SpecBuilder.<UserEntity>init()
                .isFalse(UserEntity_.deleted)
                .eq(UserEntity_.uuid, uuid)
                .build();

        return jpaUserRepository.findOne(specs)
                .map(UserMapper::fromPersistence);
    }

    @Override
    public Optional<User> findByEmailAndNotDeleted(String email) {
        Specification<UserEntity> specs = SpecBuilder.<UserEntity>init()
                .isFalse(UserEntity_.deleted)
                .eq(UserEntity_.email, email)
                .build();

        return jpaUserRepository.findOne(specs)
                .map(UserMapper::fromPersistence);
    }

    @Override
    public List<User> searchByEmailUserFilter(String email, int size) {
        Specification<UserEntity> specs = SpecBuilder.<UserEntity>init()
                .and(UserSpecs.userQueryFilter())
                .like(UserEntity_.email, email)
                .build();

        return jpaUserRepository.findAll(specs, PageRequest.of(0, size)).stream()
                .map(UserMapper::fromPersistence)
                .toList();
    }

    @Override
    public List<User> searchByNameUserFilter(String name, int Size) {
        Specification<UserEntity> specs = SpecBuilder.<UserEntity>init()
                .and(UserSpecs.userQueryFilter())
                .like(UserEntity_.fullNameLower, name.toLowerCase())
                .build();

        return jpaUserRepository.findAll(specs, PageRequest.of(0, Size)).stream()
                .map(UserMapper::fromPersistence)
                .toList();
    }

    @Override
    public List<User> searchByEmail(String email, int Size) {
        Specification<UserEntity> specs = SpecBuilder.<UserEntity>init()
                .like(UserEntity_.email, email)
                .build();

        return jpaUserRepository.findAll(specs, PageRequest.of(0, Size)).stream()
                .map(UserMapper::fromPersistence)
                .toList();
    }

    @Override
    public List<User> searchByName(String name, int Size) {
        Specification<UserEntity> specs = SpecBuilder.<UserEntity>init()
                .like(UserEntity_.fullNameLower, name.toLowerCase())
                .build();

        return jpaUserRepository.findAll(specs, PageRequest.of(0, Size)).stream()
                .map(UserMapper::fromPersistence)
                .toList();
    }
}
