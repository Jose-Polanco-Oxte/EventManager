package jpolanco.springbootapp.unit.infrastructure.user.repositorry;

import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.RoleEntity;
import jpolanco.springbootapp.user.infrastructure.adapters.output.repository.JpaRoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql(statements = {
        "INSERT INTO roles (id) VALUES ('USER');",
        "INSERT INTO roles (id) VALUES ('ADMIN');",
        "INSERT INTO roles (id) VALUES ('ORGANIZER');"
})
public class RoleRepositoryTest {

    @Autowired
    private JpaRoleRepository jpaRoleRepository;

    // Commands tests

    @Test
    public void saveRoleTest() {
        var role = jpaRoleRepository.save(
                RoleEntity.builder()
                        .name("TEST_ROLE")
                        .build()
        );

        // Assertions to verify the saved role
        assertNotNull(role);
        assertEquals("TEST_ROLE", role.getName());
    }

    @Test
    public void deleteRoleTest() {
        var role = jpaRoleRepository.save(
                RoleEntity.builder()
                        .name("TEST_ROLE")
                        .build()
        );

        jpaRoleRepository.delete(role);
        assertFalse(jpaRoleRepository.existsById(role.getName()));
    }

    // Queries tests
    @Test
    public void findByNameTest() {
        var role = jpaRoleRepository.save(
                RoleEntity.builder()
                        .name("TEST_ROLE")
                        .build()
        );

        var foundRole = jpaRoleRepository.findById("TEST_ROLE");
        assertTrue(foundRole.isPresent());
        assertEquals("TEST_ROLE", foundRole.get().getName());
    }

    @Test
    public void existsByNameTest() {
        var role = jpaRoleRepository.save(
                RoleEntity.builder()
                        .name("TEST_ROLE")
                        .build()
        );

        assertTrue(jpaRoleRepository.existsByName("TEST_ROLE"));
        assertFalse(jpaRoleRepository.existsByName("NON_EXISTENT_ROLE"));
    }
}
