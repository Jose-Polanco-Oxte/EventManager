package jpolanco.springbootapp.unit.infrastructure.user.mappers;

import jpolanco.springbootapp.unit.infrastructure.user.repositorry.TestUserFactory;
import jpolanco.springbootapp.user.domain.model.value_objects.User;
import jpolanco.springbootapp.user.infrastructure.adapters.mappers.entity.UserEntityMapperImpl;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.RoleEntity;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserEntityMapper Tests")
public class UserEntityMapperTest {

    private UserEntityMapperImpl userEntityMapperImpl;
    private User validUser;
    private UserEntity userEntity;
    private Set<RoleEntity> roles;

    @BeforeEach
    void setUp() {
        userEntityMapperImpl = new UserEntityMapperImpl();
        validUser = User.of(
                24L,
                UUID.randomUUID(),
                "John",
                "Doe",
                "example@gmail.com",
                "password123"
        ).getSuccess();
        userEntity = TestUserFactory.generateUser();
    }

    @Nested
    @DisplayName("fromDomain Method Tests")
    class UserToDomainTests {
        @Test
        @DisplayName("Test fromDomain method with valid User")
        void testToEntityWithValidUser() {
            UserEntity entity = userEntityMapperImpl.toEntity(validUser);
            assertNotNull(entity);
            assertEquals(validUser.getId(), entity.getId());
            assertEquals(validUser.getFirstName(), entity.getFirstName());
            assertEquals(validUser.getLastName(), entity.getLastName());
            assertEquals(validUser.getEmail(), entity.getEmail());
            assertEquals(validUser.getEncodedPassword(), entity.getPassword());
            assertEquals(validUser.getStatus().getValue(), entity.getStatus().name());
            assertEquals(validUser.getCreatedAt(), entity.getCreatedAt());
            assertEquals(validUser.getQRFileName(), entity.getQrFileName());
            assertEquals(new HashSet<>(validUser.getRoles()), entity.getRoles()
                    .stream()
                    .map(RoleEntity::getName)
                    .collect(Collectors.toSet()));
            assertTrue(entity.getRoles().stream().anyMatch(role -> role.getName().equals("USER")));
            assertNotNull(entity.getCreatedAt());
            assertNotNull(entity.getQrFileName());
        }

        @Test
        @DisplayName("Time performance test for fromDomain method")
        void testToEntityPerformance() {
            long startTime = System.nanoTime();
            UserEntity entity = userEntityMapperImpl.toEntity(validUser);
            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            assertTrue(duration < 1000000, "fromDomain method took too long: " + duration + " nanoseconds");
            System.out.println("fromDomain method performance: " + duration + " nanoseconds");
        }
    }

    @Nested
    @DisplayName("toDomain Method Tests")
    class UserEntityToDomainTests {
        @Test
        @DisplayName("Test toDomain method with valid UserEntity")
        void testToDomainWithValidUserEntity() {
            User user = userEntityMapperImpl.toDomain(userEntity);
            assertNotNull(user);
            assertEquals(userEntity.getId(), user.getId());
            assertEquals(userEntity.getFirstName(), user.getFirstName());
            assertEquals(userEntity.getLastName(), user.getLastName());
            assertEquals(userEntity.getEmail(), user.getEmail());
            assertEquals(userEntity.getPassword(), user.getEncodedPassword());
            assertEquals(userEntity.getStatus().name(), user.getStatus().getValue());
            assertEquals(userEntity.getCreatedAt(), user.getCreatedAt());
            assertEquals(userEntity.getQrFileName(), user.getQRFileName());
            Set<String> roles = userEntity.getRoles()
                    .stream()
                    .map(RoleEntity::getName)
                    .collect(Collectors.toSet());
            assertEquals(roles, new HashSet<>(user.getRoles()));
        }
    }
}
