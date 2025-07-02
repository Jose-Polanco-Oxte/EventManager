package jpolanco.springbootapp.user.infrastructure.services.interfaces;

import jpolanco.springbootapp.shared.application.CursorPageResult;
import jpolanco.springbootapp.shared.application.PageResult;
import jpolanco.springbootapp.user.domain.model.value_objects.User;

import java.util.Optional;
import java.util.UUID;

public interface UserQueryService {
    Optional<User> getById(UUID userId);
    Optional<User> getByEmail(String email);
    PageResult<User> getByPages(int page, int size, String sortBy, String orderBy);
    CursorPageResult<User, UUID> getByCursor(UUID cursor, int size, String sortBy, String orderBy);
}