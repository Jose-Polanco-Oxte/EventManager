package jpolanco.springbootapp.user.infrastructure.adapters.output.mysql;

import jpolanco.springbootapp.shared.application.CursorPageResult;
import jpolanco.springbootapp.shared.application.PageResult;
import jpolanco.springbootapp.shared.infrastructure.components.PageAux;
import jpolanco.springbootapp.user.application.ports.output.UserRepository;
import jpolanco.springbootapp.user.domain.model.User;
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

@RequiredArgsConstructor
@Repository
public class UserRepositoryMySQL implements UserRepository {

    private final JpaUserRepository jpaUserRepository;
    private final UserEntityMapper mapper;

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findById(String userId) {
        return jpaUserRepository.findById(UUID.fromString(userId))
                .map(mapper::toDomain);
    }

    @Override
    public User save(User user) {
        jpaUserRepository.save(mapper.toEntity(user));
        return user;
    }

    @Override
    public void deleteById(String userId) {
        jpaUserRepository.deleteById(UUID.fromString(userId));
    }

    @Override
    public User update(User entity) {
        var user = jpaUserRepository.save(mapper.toEntity(entity));
        return mapper.toDomain(user);
    }

    @Override
    public List<User> findAll() {
        return jpaUserRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<User> searchByName(String name, int size) {
        return jpaUserRepository.searchByName(name, PageRequest.of(0, size))
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<User> searchByEmail(String email, int size) {
        return jpaUserRepository.searchByEmail(email, PageRequest.of(0, size))
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public PageResult<User> getUsers(int page, int size, String sortBy, String sortOrder) {
        var sort = PageAux.resolveSort(sortBy, sortOrder);
        var pageRequest = PageRequest.of(page, size, sort);

        var users = jpaUserRepository.findAll(pageRequest);
        return new PageResult<>(
                users.get().map(mapper::toDomain).toList(),
                users.getNumber(),
                users.getSize(),
                users.getTotalElements(),
                users.getTotalPages(),
                users.hasNext()
        );
    }

    @Override
    public CursorPageResult<User, String> getUsers(String cursor, int size, String sortBy, String sortOrder) {
        var sort = PageAux.resolveSort(sortBy, sortOrder);
        var pageRequest = PageRequest.of(0, size, sort);

        Slice<UserEntity> slice;
        if (cursor.equalsIgnoreCase("none")) {
            slice = jpaUserRepository.findAll(pageRequest);
        } else {
            var cursorData = PageAux.decodeCursor(cursor);
            slice = jpaUserRepository.findAllByCursorIdAndCreatedAt(
                    cursorData.id(),
                    cursorData.date(),
                    pageRequest
            );
        }
        if (slice.isEmpty()) {
            return new CursorPageResult<>(List.of(), null, false);
        }
        var users = slice.get().map(mapper::toDomain).toList();
        var date = slice.getContent().getLast().getCreatedAt();
        var lastId = slice.getContent().getLast().getId();
        String nextCursor = PageAux.encodeCursor(date, lastId);
        return new CursorPageResult<>(
                users,
                nextCursor,
                slice.hasNext()
        );
    }
}
