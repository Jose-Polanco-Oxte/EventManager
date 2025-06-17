package jpolanco.springbootapp.user.application.ports.output;

import jpolanco.springbootapp.shared.application.utils.CursorPageResult;
import jpolanco.springbootapp.shared.application.utils.PageResult;
import jpolanco.springbootapp.shared.application.adapters.output.CRUDRepository;
import jpolanco.springbootapp.user.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CRUDRepository<User, String> {

    Optional<User> findByEmail(String email);

    List<User> findAll();

    List<User> searchByName(String name, int numberOfResults);

    List<User> searchByEmail(String email, int numberOfResults);

    PageResult<User> getUsers(int page, int size, String sortBy, String sortOrder);

    CursorPageResult<User, String> getUsers(String cursor, int size, String sortBy, String sortOrder);
}