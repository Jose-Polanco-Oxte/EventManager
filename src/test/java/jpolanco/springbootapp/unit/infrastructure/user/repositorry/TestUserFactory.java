package jpolanco.springbootapp.unit.infrastructure.user.repositorry;

import jpolanco.springbootapp.shared.utils.results.Report;
import jpolanco.springbootapp.shared.utils.results.SuperResult;
import jpolanco.springbootapp.user.domain.model.valueobjects.User;
import jpolanco.springbootapp.user.domain.model.valueobjects.UserStatus;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.RoleEntity;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.UserEntity;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class TestUserFactory {
    private static final RoleEntity roleUser = RoleEntity.builder().id(1L).name("USER").build();
    private static final RoleEntity roleAdmin = RoleEntity.builder().id(2L).name("ADMIN").build();
    private static final RoleEntity roleOrganizer = RoleEntity.builder().id(3L).name("ORGANIZER").build();
    private static final Instant baseTime = Instant.parse("2025-07-01T12:00:00Z");

    public static UserEntity generateUser() {
        return UserEntity.builder()
                .uuid(UUID.randomUUID())
                .email("alice@example.com")
                .firstName("Alice")
                .lastName("Smith")
                .password("password123")
                .roles(Set.of(roleUser))
                .createdAt(baseTime.plusSeconds(100))
                .qrFileName("qr_alice")
                .status(UserStatus.ACTIVE)
                .build();
    }

    public static SuperResult<User, Report> generateUserDomain() {
        return User.create(
                "John",
                "Doe",
                "johnDoe@gmail.com",
                "password123"
        );
    }

    public static List<User> generateUserDomainList() {
        return List.of(
                User.create("Alice", "Smith", "example1@gmail.com", "password123").getSuccess(),
                User.create("Bob", "Johnson", "example2@gmail.com", "password123").getSuccess(),
                User.create("Carol", "Williams", "example3@gmail.com", "password123").getSuccess()
        );
    }

    public static List<UserEntity> generateUsers() {

        return List.of(
                UserEntity.builder()
                        .uuid(UUID.randomUUID())
                        .email("alice@example.com")
                        .firstName("Alice")
                        .lastName("Smith")
                        .password("password123")
                        .roles(Set.of(roleUser))
                        .createdAt(baseTime.plusSeconds(100))
                        .qrFileName("qr_alice")
                        .status(UserStatus.ACTIVE)
                        .build(),

                UserEntity.builder()
                        .uuid(UUID.randomUUID())
                        .email("bob@example.com")
                        .firstName("Bob")
                        .lastName("Johnson")
                        .password("password123")
                        .roles(Set.of(roleAdmin))
                        .createdAt(baseTime.plusSeconds(200))
                        .qrFileName("qr_bob")
                        .status(UserStatus.ACTIVE)
                        .build(),

                UserEntity.builder()
                        .uuid(UUID.randomUUID())
                        .email("carol@example.com")
                        .firstName("Carol")
                        .lastName("Williams")
                        .password("password123")
                        .roles(Set.of(roleOrganizer))
                        .createdAt(baseTime.plusSeconds(50))
                        .qrFileName("qr_carol")
                        .status(UserStatus.ACTIVE)
                        .build(),

                UserEntity.builder()
                        .uuid(UUID.randomUUID())
                        .email("david@example.com")
                        .firstName("David")
                        .lastName("Brown")
                        .password("password123")
                        .roles(Set.of(roleUser, roleOrganizer))
                        .createdAt(baseTime.plusSeconds(700))
                        .qrFileName("qr_david")
                        .status(UserStatus.ACTIVE)
                        .build(),

                UserEntity.builder()
                        .uuid(UUID.randomUUID())
                        .email("emma@example.com")
                        .firstName("Emma")
                        .lastName("Davis")
                        .password("password123")
                        .roles(Set.of(roleUser, roleAdmin))
                        .createdAt(baseTime.plusSeconds(500))
                        .qrFileName("qr_emma")
                        .status(UserStatus.ACTIVE)
                        .build()
        );
    }
}
