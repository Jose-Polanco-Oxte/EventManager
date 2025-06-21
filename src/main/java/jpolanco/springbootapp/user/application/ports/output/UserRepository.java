package jpolanco.springbootapp.user.application.ports.output;

import jpolanco.springbootapp.shared.utils.CursorPageResult;
import jpolanco.springbootapp.shared.utils.PageResult;
import jpolanco.springbootapp.shared.application.adapters.output.CRUDRepository;
import jpolanco.springbootapp.user.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CRUDRepository<User, String> {

    Optional<User> findByEmail(String email);

    List<User> findAll();

    PageResult<User> findAll(int page, int size, String sortBy, String sortOrder);

    CursorPageResult<User, String> findAll(String cursor, int size, String sortBy, String sortOrder);

    List<User> searchByName(String name, int size);

    List<User> searchByEmail(String email, int size);
}