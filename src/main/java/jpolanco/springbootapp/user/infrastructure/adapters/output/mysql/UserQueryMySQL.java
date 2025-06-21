package jpolanco.springbootapp.user.infrastructure.adapters.output.mysql;

import jpolanco.springbootapp.shared.infrastructure.components.PageAux;
import jpolanco.springbootapp.shared.utils.CursorPageResult;
import jpolanco.springbootapp.shared.utils.PageResult;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
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

@Repository
@RequiredArgsConstructor
public class UserQueryMySQL implements UserQueryRepository {
    private final JpaUserRepository jpaUserRepository;
    private final UserEntityMapper mapper;

    @Override
    public Optional<User> findById(String id) {
        return jpaUserRepository.findById(UUID.fromString(id))
                .map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email).map(mapper::toDomain);
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
    public PageResult<User> findAll(int page, int size, String sortBy, String sortOrder) {
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
    public CursorPageResult<User, String> findAll(String cursor, int size, String sortBy, String sortOrder) {
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
