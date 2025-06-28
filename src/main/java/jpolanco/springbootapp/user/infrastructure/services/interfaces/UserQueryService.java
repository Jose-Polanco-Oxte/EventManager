package jpolanco.springbootapp.user.infrastructure.services.interfaces;

import jpolanco.springbootapp.shared.application.CursorPageResult;
import jpolanco.springbootapp.shared.application.PageResult;
import jpolanco.springbootapp.user.domain.model.User;

import java.util.Optional;

public interface UserQueryService {
    Optional<User> getById(String userId);
    Optional<User> getByEmail(String email);
    PageResult<User> getByPages(int page, int size, String sortBy, String orderBy);
    CursorPageResult<User, String> getByCursor(String cursor, int size, String sortBy, String orderBy);
}