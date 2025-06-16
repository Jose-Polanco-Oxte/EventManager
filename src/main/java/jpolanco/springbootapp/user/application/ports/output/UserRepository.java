package jpolanco.springbootapp.user.application.ports.output;

import jpolanco.springbootapp.shared.application.CursorPageResult;
import jpolanco.springbootapp.shared.application.PageResult;
import jpolanco.springbootapp.shared.domain.CRUDRepository;
import jpolanco.springbootapp.user.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CRUDRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findById(String userId);

    User save(User user);

    void deleteById(String userId);

    List<User> findAll();

    List<User> searchByName(String name, int numberOfResults);

    List<User> searchByEmail(String email, int numberOfResults);

    PageResult<User> getUsers(int page, int size, String sortBy, String sortOrder);

    CursorPageResult<User, String> getUsers(String cursor, int size, String sortBy, String sortOrder);
}