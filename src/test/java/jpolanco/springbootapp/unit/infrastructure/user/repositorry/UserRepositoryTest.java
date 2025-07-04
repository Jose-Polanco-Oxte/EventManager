package jpolanco.springbootapp.unit.infrastructure.user.repositorry;

import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.UserEntity;
import jpolanco.springbootapp.user.infrastructure.adapters.output.repository.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql(statements = {
        "INSERT INTO roles (id, name, description, name_lower) VALUES (1L, 'USER', 'Standard user', 'user');",
        "INSERT INTO roles (id, name, description, name_lower) VALUES (2L, 'ADMIN', 'System administrator', 'admin');",
        "INSERT INTO roles (id, name, description, name_lower) VALUES (3L, 'ORGANIZER', 'Event manager', 'organizer');",
})
public class UserRepositoryTest {

    @Autowired
    private JpaUserRepository jpaUserRepository;

    private UserEntity user;
    private List<UserEntity> users;

    @BeforeEach
    public void setUpd() {
        user = TestUserFactory.generateUser();
        users = TestUserFactory.generateUsers();
    }

    // Commands tests

    @Test
    public void saveUserTest() {
        var savedUser = jpaUserRepository.save(user);

        // Assertions to verify the saved user
        assertNotNull(savedUser);
        assertEquals(user.getId(), savedUser.getId());
        assertEquals(user.getEmail(), savedUser.getEmail());
        assertEquals(user.getFirstName(), savedUser.getFirstName());
        assertEquals(user.getLastName(), savedUser.getLastName());
        assertEquals(user.getPassword(), savedUser.getPassword());
        assertEquals(user.getRoles(), savedUser.getRoles());
        assertEquals(user.getStatus(), savedUser.getStatus());
    }

    @Test
    public void deleteUserByIdTest() {
        var savedUser = jpaUserRepository.save(user);
        jpaUserRepository.deleteById(savedUser.getId());

        // Assertions to verify the user is deleted
        assertFalse(jpaUserRepository.findById(savedUser.getId()).isPresent());
    }

    // Queries tests

    @Test
    public void findByIdTest() {
        var savedUser = jpaUserRepository.save(user);
        var foundUser = jpaUserRepository.findById(savedUser.getId());

        // Assertions to verify the found user
        assertTrue(foundUser.isPresent());
        assertEquals(savedUser.getId(), foundUser.get().getId());
        assertEquals(savedUser.getEmail(), foundUser.get().getEmail());
        assertEquals(savedUser.getFirstName(), foundUser.get().getFirstName());
        assertEquals(savedUser.getLastName(), foundUser.get().getLastName());
        assertEquals(savedUser.getPassword(), foundUser.get().getPassword());
        assertEquals(savedUser.getRoles(), foundUser.get().getRoles());
        assertEquals(savedUser.getStatus(), foundUser.get().getStatus());
    }

    @Test
    public void findByEmailTest() {
        jpaUserRepository.save(user);
        var foundUser = jpaUserRepository.findByEmail(user.getEmail());

        // Assertions to verify the found user by email
        assertTrue(foundUser.isPresent());
        assertEquals(user.getId(), foundUser.get().getId());
        assertEquals(user.getEmail(), foundUser.get().getEmail());
        assertEquals(user.getFirstName(), foundUser.get().getFirstName());
        assertEquals(user.getLastName(), foundUser.get().getLastName());
        assertEquals(user.getPassword(), foundUser.get().getPassword());
        assertEquals(user.getRoles(), foundUser.get().getRoles());
        assertEquals(user.getStatus(), foundUser.get().getStatus());
    }

    @Test
    public void searchByNameTest() {
        jpaUserRepository.saveAll(users);
        var foundUsers = jpaUserRepository.searchByName("Dav", PageRequest.of(0, 10));

        // Assertions to verify the found users by name
        assertNotNull(foundUsers);
        assertFalse(foundUsers.isEmpty());
        assertTrue(foundUsers.stream().allMatch(user -> user.getFirstName().contains("Dav") || user.getLastName().contains("Dav")));
        assertEquals(2, foundUsers.size());
        assertEquals("David", foundUsers.getFirst().getFirstName());
        assertEquals("Davis", foundUsers.getLast().getLastName());
        assertEquals("Brown", foundUsers.getFirst().getLastName());
        assertEquals("Emma", foundUsers.getLast().getFirstName());
    }

    @Test
    public void searchByEmailTest() {
        jpaUserRepository.saveAll(users);
        var foundUsers = jpaUserRepository.searchByEmail("a", PageRequest.of(0, 3));

        // Assertions to verify the found users by email
        assertNotNull(foundUsers);
        assertFalse(foundUsers.isEmpty());
        assertTrue(foundUsers.stream().allMatch(user -> user.getEmail().contains("a")));
        assertEquals(3, foundUsers.size());
    }

    @Test
    public void findAllByPagesTest() {
        jpaUserRepository.saveAll(users);
        var pageResult = jpaUserRepository.findAll(PageRequest.of(2, 2, Sort.by(Sort.Direction.DESC, "createdAt")));

        // Assertions to verify the found users
        assertNotNull(pageResult);
        assertFalse(pageResult.isEmpty());
        assertEquals(1, pageResult.getContent().size());
        assertEquals(2, pageResult.getSize());
        assertEquals(5, pageResult.getTotalElements());
        assertEquals(3, pageResult.getTotalPages());
        assertFalse(pageResult.hasNext());

        var pageResult2 = jpaUserRepository.findAll(PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdAt")));

        // Assertions to verify the found users
        assertNotNull(pageResult2);
        assertFalse(pageResult2.isEmpty());
        assertEquals(2, pageResult2.getContent().size());
        assertEquals(2, pageResult2.getSize());
        assertEquals(5, pageResult2.getTotalElements());
        assertEquals(3, pageResult2.getTotalPages());
        assertTrue(pageResult2.hasNext());
    }

    @Test
    public void findAllByCursorTest() {
        jpaUserRepository.saveAll(users);
        var userId = users.getFirst().getId();
        var pageResult = jpaUserRepository.findByIdGreaterThan(userId, PageRequest.of(0, 2));
        // Assertions to verify the found users
        assertNotNull(pageResult);
        assertFalse(pageResult.isEmpty());
        assertEquals(2, pageResult.getContent().size());
        assertEquals(2, pageResult.getSize());
        assertTrue(pageResult.hasNext());
    }
}
