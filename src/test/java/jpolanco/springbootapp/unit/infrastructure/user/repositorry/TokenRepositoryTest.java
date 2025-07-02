package jpolanco.springbootapp.unit.infrastructure.user.repositorry;

import jakarta.persistence.EntityManager;
import jpolanco.springbootapp.user.application.utils.TokenStatus;
import jpolanco.springbootapp.user.application.utils.TokenType;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.TokenEntity;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.UserEntity;
import jpolanco.springbootapp.user.infrastructure.adapters.output.repository.JpaTokenRepository;
import jpolanco.springbootapp.user.infrastructure.adapters.output.repository.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
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
public class TokenRepositoryTest {

    @Autowired
    private JpaTokenRepository jpaTokenRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    private UserEntity user;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void setUp() {

        user = TestUserFactory.generateUsers().getLast();
        user = jpaUserRepository.save(user);
    }

    // Queries tests

    @Test
    public void findByIdTest() {
        var token = jpaTokenRepository.save(
                TokenEntity.builder()
                        .token("test-token")
                        .type(TokenType.BEARER)
                        .status(TokenStatus.ACTIVE)
                        .user(user)
                        .build()
        );

        var foundToken = jpaTokenRepository.findById(token.getId());
        assertTrue(foundToken.isPresent());
        assertEquals(token.getId(), foundToken.get().getId());
        assertEquals("test-token", foundToken.get().getToken());
        assertEquals(TokenType.BEARER, foundToken.get().getType());
        assertEquals(TokenStatus.ACTIVE, foundToken.get().getStatus());
        assertEquals(user.getId(), foundToken.get().getUser().getId());
    }

    @Test
    public void findByTokenTest() {
        var token = jpaTokenRepository.save(
                TokenEntity.builder()
                        .token("test-token")
                        .type(TokenType.BEARER)
                        .status(TokenStatus.ACTIVE)
                        .user(user)
                        .build()
        );

        var foundToken = jpaTokenRepository.findByToken("test-token");
        assertTrue(foundToken.isPresent());
        assertEquals(token.getId(), foundToken.get().getId());
    }

    @Test
    public void findAllByUserIdTest() {
        var token1 = jpaTokenRepository.save(
                TokenEntity.builder()
                        .token("test-token-1")
                        .type(TokenType.BEARER)
                        .status(TokenStatus.ACTIVE)
                        .user(user)
                        .build()
        );
        var token2 = jpaTokenRepository.save(
                TokenEntity.builder()
                        .token("test-token-2")
                        .type(TokenType.BEARER)
                        .status(TokenStatus.ACTIVE)
                        .user(user)
                        .build()
        );

        var tokens = jpaTokenRepository.findAllByUserId(user.getId());
        assertEquals(2, tokens.size());
        assertTrue(tokens.stream().anyMatch(t -> t.getId().equals(token1.getId())));
        assertTrue(tokens.stream().anyMatch(t -> t.getId().equals(token2.getId())));
    }

    @Test
    public void countByUserIdAndStatusIsActiveTest() {
        var token1 = jpaTokenRepository.save(
                TokenEntity.builder()
                        .token("test-token-1")
                        .type(TokenType.BEARER)
                        .status(TokenStatus.ACTIVE)
                        .user(user)
                        .build()
        );
        var token2 = jpaTokenRepository.save(
                TokenEntity.builder()
                        .token("test-token-2")
                        .type(TokenType.BEARER)
                        .status(TokenStatus.REVOKED)
                        .user(user)
                        .build()
        );

        var token3 = jpaTokenRepository.save(
                TokenEntity.builder()
                        .token("test-token-3")
                        .type(TokenType.BEARER)
                        .status(TokenStatus.ACTIVE)
                        .user(user)
                        .build()
        );

        int activeCount = jpaTokenRepository.countByUserIdAndStatusIsActive(user.getId());
        assertEquals(2, activeCount);
    }

    // Commands tests

    @Test
    public void saveTokenTest() {
        var token = jpaTokenRepository.save(
                TokenEntity.builder()
                        .token("test-token")
                        .type(TokenType.BEARER)
                        .status(TokenStatus.ACTIVE)
                        .user(user)
                        .build()
        );

        // Assertions to verify the saved token
        assertNotNull(token);
        assertEquals("test-token", token.getToken());
        assertEquals(user.getId(), token.getUser().getId());
        assertEquals(TokenType.BEARER, token.getType());
        assertEquals(TokenStatus.ACTIVE, token.getStatus());
    }

    @Test
    public void deleteTokenTest() {
        var token = jpaTokenRepository.save(
                TokenEntity.builder()
                        .token("test-token-to-delete")
                        .type(TokenType.BEARER)
                        .status(TokenStatus.ACTIVE)
                        .user(user)
                        .build()
        );

        jpaTokenRepository.delete(token);
        assertFalse(jpaTokenRepository.existsById(token.getId()));
    }

    @Test
    public void deleteAllByUserIdTest() {
        var token1 = jpaTokenRepository.save(
                TokenEntity.builder()
                        .token("test-token-1")
                        .type(TokenType.BEARER)
                        .status(TokenStatus.ACTIVE)
                        .user(user)
                        .build()
        );

        var token2 = jpaTokenRepository.save(
                TokenEntity.builder()
                        .token("test-token-2")
                        .type(TokenType.BEARER)
                        .status(TokenStatus.ACTIVE)
                        .user(user)
                        .build()
        );

        jpaTokenRepository.deleteAllByUserId(user.getId());
        assertFalse(jpaTokenRepository.existsById(token1.getId()));
        assertFalse(jpaTokenRepository.existsById(token2.getId()));
    }

    @Test
    public void deleteAllByStatus() {
        var token1 = jpaTokenRepository.save(
                TokenEntity.builder()
                        .token("test-token-3")
                        .type(TokenType.BEARER)
                        .status(TokenStatus.ACTIVE)
                        .user(user)
                        .build()
        );
        var token2 = jpaTokenRepository.save(
                TokenEntity.builder()
                        .token("test-token-4")
                        .type(TokenType.BEARER)
                        .status(TokenStatus.REVOKED)
                        .user(user)
                        .build()
        );
        var token3 = jpaTokenRepository.save(
                TokenEntity.builder()
                        .token("test-token-5")
                        .type(TokenType.BEARER)
                        .status(TokenStatus.REVOKED)
                        .user(user)
                        .build()
        );
        jpaTokenRepository.deleteAllByStatus(TokenStatus.REVOKED);
        assertTrue(jpaTokenRepository.existsById(token1.getId()));
        assertFalse(jpaTokenRepository.existsById(token2.getId()));
    }

    @Test
    public void revokeAllByUserIdTest() {
        var token1 = jpaTokenRepository.save(
                TokenEntity.builder()
                        .token("test-token-6")
                        .type(TokenType.BEARER)
                        .status(TokenStatus.ACTIVE)
                        .user(user)
                        .build()
        );
        var token2 = jpaTokenRepository.save(
                TokenEntity.builder()
                        .token("test-token-7")
                        .type(TokenType.BEARER)
                        .status(TokenStatus.ACTIVE)
                        .user(user)
                        .build()
        );

        entityManager.flush();
        entityManager.clear();

        int updatedCount = jpaTokenRepository.revokeAllByUserId(user.getId());
        assertEquals(2, updatedCount);
        assertEquals(TokenStatus.REVOKED, jpaTokenRepository.findById(token1.getId()).get().getStatus());
        assertEquals(TokenStatus.REVOKED, jpaTokenRepository.findById(token2.getId()).get().getStatus());
    }

    @Test
    public void deleteByTokenTest() {
        var token = jpaTokenRepository.save(
                TokenEntity.builder()
                        .token("test-token-to-delete-by-token")
                        .type(TokenType.BEARER)
                        .status(TokenStatus.ACTIVE)
                        .user(user)
                        .build()
        );

        jpaTokenRepository.deleteByToken(token.getToken());
        assertFalse(jpaTokenRepository.existsById(token.getId()));
    }
}
