package jpolanco.springbootapp.unit.application;

import jpolanco.springbootapp.event.application.ports.input.request.CursorPaginationRequest;
import jpolanco.springbootapp.event.application.ports.input.request.PagePaginationRequest;
import jpolanco.springbootapp.shared.utils.CursorPageResult;
import jpolanco.springbootapp.shared.utils.PageResult;
import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.services.unique.GetUserByEmail;
import jpolanco.springbootapp.user.application.services.unique.GetUserById;
import jpolanco.springbootapp.user.application.services.unique.GetUsers;
import jpolanco.springbootapp.user.domain.model.User;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class UserQueryUCTest {
    /**
     * This class contains unit tests for the UserQuery Use Cases.
     * It tests the following use cases:
     * - GetUserById: retrieves a user by their ID.
     * - GetUserByEmail: retrieves a user by their email.
     * - GetUsers: retrieves a paginated list of users using both page-based and cursor-based pagination.
     * These tests ensure that the use cases correctly interact with the UserQueryRepository
     * and return the expected results.
     * * The tests use Mockito to mock the UserQueryRepository and verify that the methods
     * are called with the correct parameters.
     * * The tests also verify that the returned results match the expected User objects.
     * * The tests cover both the retrieval of a single user by ID and email, as well as
     * the retrieval of multiple users with pagination.
     * * The tests are organized into nested classes for better readability and organization.
     */

    // Mocking the repositories
    @Mock
    private UserQueryRepository userQueryRepository;

    // Utils
    private String testId;

    // Use case to be tested
    private GetUserById getUserById;
    private GetUserByEmail getUserByEmail;
    private GetUsers getUsers;

    @BeforeEach
    public void setUp() {
        getUserById = new GetUserById(userQueryRepository);
        getUserByEmail = new GetUserByEmail(userQueryRepository);
        getUsers = new GetUsers(userQueryRepository);
        // Common UUID for testing
        testId = UUID.randomUUID().toString();
    }

    @Test
    @DisplayName("When getUserById is called, it should return an optional User")
    public void getUserById() {
        // Creating simulated user data
        Optional<User> simulatedUser = Optional.of(User
                .of(testId, "John", "Doe", "johnDoe@gmail.com", "password123")
                .getValue());

        // Expected user creation
        Optional<User> expectedUser = Optional.of(User
                .of(testId, "John", "Doe", "johnDoe@gmail.com", "password123")
                .getValue());

        // Mocking the repository call
        Mockito.when(userQueryRepository.findById(testId))
                .thenReturn(simulatedUser);

        // Calling the use case method
        final Optional<User> result = getUserById.get(testId);

        // Verifying the interaction with the repository
        assertEquals(expectedUser, result);
        Mockito.verify(userQueryRepository, Mockito.times(1)).findById(testId);
    }

    @Test
    @DisplayName("When getUserByEmail is called, it should return an optional User")
    public void getUserByEmail() {
        // Common email for testing
        String testEmail = "johnDoe@gmail.com";

        // Creating simulated user data
        Optional<User> simulatedUser = Optional.of(User
                .of(testId, "John", "Doe", testEmail, "password123")
                .getValue());

        // Expected user creation
        Optional<User> expectedUser = Optional.of(User
                .of(testId, "John", "Doe", testEmail, "password123")
                .getValue());

        // Mocking the repository call
        Mockito.when(userQueryRepository.findByEmail(testEmail))
                .thenReturn(simulatedUser);

        // Calling the use case method
        final Optional<User> result = getUserByEmail.get(testEmail);

        // Verifying the interaction with the repository
        assertEquals(expectedUser, result);
        Mockito.verify(userQueryRepository, Mockito.times(1)).findByEmail(testEmail);
    }

    @Nested
    @DisplayName("GetUsers Use Case Tests")
    class GetUsersTests {

        private List<User> users;

        @BeforeEach
        public void setUp() {
            this.users = List.of(
                    User.of(UUID.randomUUID().toString(), "Alice", "Smith", "alice@gmail.com", "7fawfwa89").getValue(),
                    User.of(UUID.randomUUID().toString(), "Bob", "Smith", "bob@gmail.com", "0awfawfa12").getValue()
            );
        }
        @Test
        @DisplayName("When getUsers is called, it should return a PageResult of Users")
        public void getUsersByPages() {
            // Generating a PagePaginationRequest
            PagePaginationRequest request = new PagePaginationRequest(
                    1,
                    10,
                    "name",
                    "asc"
            );

            // Expected PageResult creation
            PageResult<User> expectedPageResult = new PageResult<>(
                    List.copyOf(users),
                    1,
                    10,
                    2,
                    1,
                    false
            );

            // Mocking the repository call
            Mockito.when(userQueryRepository.findAll(
                    request.page(),
                    request.size(),
                    request.sortBy(),
                    request.orderBy()
            )).thenReturn(expectedPageResult);

            // Calling the use case method
            PageResult<User> result = getUsers.getByPages(request);

            // Verifying the interaction with the repository
            assertEquals(expectedPageResult, result);
            assertEquals(1, result.getTotalPages());
            assertEquals(2, result.getTotalElements());
            assertFalse(result.isHasNext());
            Mockito.verify(userQueryRepository, Mockito.times(1)).findAll(
                    request.page(),
                    request.size(),
                    request.sortBy(),
                    request.orderBy()
            );
        }

        @Test
        @DisplayName("When getUsers is called, it should return CursorPageResult of Users")
        public void getUsersByCursor() {
            // Generating a CursorPaginationRequest
            String cursor = UUID.randomUUID().toString();
            CursorPaginationRequest <String> request = new CursorPaginationRequest<>(
                    cursor,
                    10,
                    "name",
                    "asc"
            );

            // Expected CursorPageResult creation
            CursorPageResult<User, String> expectedCursorPageResult = new CursorPageResult<>(
                    List.copyOf(users),
                    users.getLast().getId(),
                    false
            );

            // Mocking the repository call
            Mockito.when(userQueryRepository.findAll(
                    request.cursor(),
                    request.size(),
                    request.sortBy(),
                    request.orderBy()
            )).thenReturn(expectedCursorPageResult);

            // Calling the use case method
            CursorPageResult<User, String> result = getUsers.getByCursor(request);

            // Verifying the interaction with the repository
            assertEquals(expectedCursorPageResult, result);
            assertEquals(users.getLast().getId(), result.lastItemId());
            assertFalse(result.hasNextPage());
            Mockito.verify(userQueryRepository, Mockito.times(1)).findAll(
                    request.cursor(),
                    request.size(),
                    request.sortBy(),
                    request.orderBy()
            );
        }
    }
}
