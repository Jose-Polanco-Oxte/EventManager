package jpolanco.springbootapp.user.infrastructure.services.interfaces;

import jpolanco.springbootapp.shared.application.pagination.CursorPageResult;
import jpolanco.springbootapp.shared.application.pagination.PageResult;
import jpolanco.springbootapp.user.domain.model.valueobjects.User;

import java.util.Optional;
import java.util.UUID;

public interface UserQueryService {
    Optional<User> getById(UUID userId);
    Optional<User> getByEmail(String email);
    PageResult<User> getByPages(int page, int size, String sortBy, String orderBy);
    CursorPageResult<User, UUID> getByCursor(UUID cursor, int size, String sortBy, String orderBy);
}