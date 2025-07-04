package jpolanco.springbootapp.user.infrastructure.adapters.output.mysql;

import jpolanco.springbootapp.shared.infrastructure.components.PageAux;
import jpolanco.springbootapp.shared.application.pagination.CursorPageResult;
import jpolanco.springbootapp.shared.application.pagination.PageResult;
import jpolanco.springbootapp.user.infrastructure.adapters.output.context.UserContext;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.domain.model.valueobjects.User;
import jpolanco.springbootapp.user.infrastructure.adapters.mappers.entity.UserEntityMapper;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.UserEntity;
import jpolanco.springbootapp.user.infrastructure.adapters.output.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserQueryMySQL implements UserQueryRepository {
    private final JpaUserRepository jpaUserRepository;
    private final UserEntityMapper mapper;
//    private final UserAggregateContext context;
    private final UserContext context;

    @Override
    public Optional<User> findById(Long userId) {
        var userEntity = jpaUserRepository.findById(userId);
        var user = userEntity.map(mapper::fromPersistence);
        user.ifPresent(u -> context.track(u, userEntity.get()));
        return user;
    }

    @Override
    public Optional<User> findByUuid(UUID uuid) {
        var userEntity = jpaUserRepository.findByUuid(uuid);
        var user = userEntity.map(mapper::fromPersistence);
        user.ifPresent(u -> context.track(u, userEntity.get()));
        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        var userEntity = jpaUserRepository.findByEmail(email);
        var user = userEntity.map(mapper::fromPersistence);
        user.ifPresent(u -> context.track(u, userEntity.get()));
        return user;
    }

    @Override
    public List<User> searchByName(String name, int size) {
        return jpaUserRepository.searchByName(name, PageRequest.of(0, size))
                .stream()
                .map(mapper::fromPersistence)
                .toList();
    }

    @Override
    public List<User> searchByEmail(String email, int size) {
        return jpaUserRepository.searchByEmail(email, PageRequest.of(0, size))
                .stream()
                .map(mapper::fromPersistence)
                .toList();
    }

    @Override
    public PageResult<User> findAll(int page, int size, String sortBy, String sortOrder) {
        var sort = PageAux.resolveSort(sortBy, sortOrder);
        var pageRequest = PageRequest.of(page, size, sort);

        var users = jpaUserRepository.findAll(pageRequest);
        return new PageResult<>(
                users.get().map(mapper::fromPersistence).toList(),
                users.getNumber(),
                users.getSize(),
                users.getTotalElements(),
                users.getTotalPages(),
                users.hasNext()
        );
    }

    @Override
    public CursorPageResult<User, UUID> findAll(UUID cursor, int size, String sortBy, String sortOrder) {
        var sort = PageAux.resolveSort(sortBy, sortOrder);
        var pageRequest = PageRequest.of(0, size, sort);
        Slice<UserEntity> slice;
        if (cursor == null) {
            slice = jpaUserRepository.findAll(pageRequest);
        } else {
            var userEntity = jpaUserRepository.findByUuid(cursor);
            if (userEntity.isEmpty()) {
                return new CursorPageResult<>(List.of(), null, false);
            }
            slice = jpaUserRepository.findByIdGreaterThan(
                    userEntity.get().getId(),
                    pageRequest
            );
        }
        if (slice.isEmpty()) {
            return new CursorPageResult<>(List.of(), null, false);
        }
        var users = slice.get().map(mapper::fromPersistence).toList();
        var lastId = slice.getContent().getLast().getUuid();
        return new CursorPageResult<>(
                users,
                lastId,
                slice.hasNext()
        );
    }
}
