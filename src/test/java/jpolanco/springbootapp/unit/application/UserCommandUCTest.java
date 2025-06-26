package jpolanco.springbootapp.unit.application;

import jpolanco.springbootapp.shared.domain.Report;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.application.ports.input.EncoderProvider;
import jpolanco.springbootapp.user.application.ports.input.QRProvider;
import jpolanco.springbootapp.user.application.ports.output.JwtQueryRepository;
import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.services.base.DeactivateUser;
import jpolanco.springbootapp.user.application.services.base.ReactivateUser;
import jpolanco.springbootapp.user.application.services.base.SuspendUser;
import jpolanco.springbootapp.user.application.services.base.UpdateUser;
import jpolanco.springbootapp.user.application.services.unique.CreateUser;
import jpolanco.springbootapp.user.application.services.unique.Login;
import jpolanco.springbootapp.user.application.utils.UserValidation;

import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.AnyUserUpdateRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.RegisterRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.UserRoleChangeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserCommandUCTest {


    // Mocking the repositories
    @Mock
    private UserCommandRepository userCommandRepository;
    @Mock
    private UserQueryRepository userQueryRepository;
    @Mock
    private JwtQueryRepository jwtQueryRepository;

    // Mocking providers
    @Mock
    private EncoderProvider encoderProvider;
    @Mock
    private QRProvider qrProvider;

    // Utils
    private String testId;

    // Use case to be tested
    private CreateUser createUser;
    private DeactivateUser deactivateUser;
    private ReactivateUser reactivateUser;
    private SuspendUser suspendUser;
    private UpdateUser updateUser;
    private Login login;

    @BeforeEach
    public void setUp() {
        // Initialize the use cases with the mocked repository
        var userValidation = new UserValidation(userQueryRepository, encoderProvider);
        createUser = new CreateUser(userCommandRepository, encoderProvider, qrProvider, userValidation);
        deactivateUser = new DeactivateUser(userQueryRepository, userCommandRepository);
        reactivateUser = new ReactivateUser(userQueryRepository, userCommandRepository);
        suspendUser = new SuspendUser(userQueryRepository, userCommandRepository);
        updateUser = new UpdateUser(userQueryRepository, userCommandRepository, encoderProvider, qrProvider);
        login = new Login(userQueryRepository, jwtQueryRepository, encoderProvider);
        // Common UUID for testing
        testId = UUID.randomUUID().toString();
    }

    @Test
    @DisplayName("CreateUser should create a user successfully")
    public void testCreateUser() {
        // Generating register dto request
        RegisterRequest request = new RegisterRequest(
                "John",
                "Doe",
                "johnDoe@gmail.com",
                "password123"
        );

        // Expected user creation
        Result<User> expectedUser = Result.success(
                User.of(
                        testId,
                        request.firstName(),
                        request.lastName(),
                        request.email(),
                        "encodedPassword123" // This will be set by the mocked encoderProvider
                ).getValue()
        );

        // Mocking the repository call
        Mockito.when(userCommandRepository.save(Mockito.any(User.class)))
                .thenAnswer(invocation -> {
                    invocation.getArgument(0);
                    User user = invocation.getArgument(0);
                    return User.of(
                            testId,
                            user.getFirstName(),
                            user.getLastName(),
                            user.getEmail(),
                            user.getEncodedPassword()
                    ).getValue();
                });

        // Mocking the QR generation
        Mockito.doNothing().when(qrProvider).generate(Mockito.anyString(), Mockito.anyString());
        // Mocking the password encoding
        Mockito.when(encoderProvider.encode(Mockito.anyString()))
                .thenReturn("encodedPassword123");
        Mockito.when(userQueryRepository.findByEmail(Mockito.anyString()))
                .thenReturn(Optional.empty());

        // Calling the use case method
        var startTime = System.currentTimeMillis();
        Result<User> result = createUser.create(request);
        System.out.printf("Total time taken for user creation: %dms%n", System.currentTimeMillis() - startTime);

        // Verifying the interaction with the repository
        Mockito.verify(userQueryRepository, Mockito.times(1)).findByEmail(Mockito.anyString());
        Mockito.verify(userCommandRepository, Mockito.times(1)).save(Mockito.any(User.class));
        Mockito.verify(qrProvider, Mockito.times(1)).generate(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(encoderProvider, Mockito.times(1)).encode(Mockito.anyString());

        // Asserting the result
        assertEquals(expectedUser, result, "Expected user creation result to match the expected value");
        assertTrue(result.isSuccess());
        assertEquals(expectedUser.getValue().getId(), result.getValue().getId());
        assertNotNull(result.getValue());
        assertEquals(request.firstName(), result.getValue().getFirstName());
        assertEquals(request.lastName(), result.getValue().getLastName());
        assertEquals(request.email(), result.getValue().getEmail());
        assertEquals("encodedPassword123", result.getValue().getEncodedPassword());
    }

    @Test
    @DisplayName("DeactivateUser should deactivate a user successfully")
    public void testDeactivateUser() {
        // Expected user deactivation
        User user = User.of(
                testId,
                "John",
                "Doe",
                "johnDoe@gmail.com",
                "encodedPassword123"
        ).getValue();
        user.deactivate("User requested deactivation");
        Result<User> expectedUser = Result.success(user);
        // Mocking the repository call
        Mockito.when(userQueryRepository.findById(testId))
                .thenReturn(Optional.of(user));
        Mockito.when(userCommandRepository.save(Mockito.any(User.class)))
                .thenAnswer(invocation -> invocation.<User>getArgument(0));

        // Calling the use case method
        var startTime = System.currentTimeMillis();
        Result<User> result = deactivateUser.deactivateById(testId, "User requested deactivation");
        System.out.printf("Total time taken for user deactivation: %dms%n", System.currentTimeMillis() - startTime);

        // Verifying the interaction with the repository
        Mockito.verify(userQueryRepository, Mockito.times(1)).findById(testId);
        Mockito.verify(userCommandRepository, Mockito.times(1)).save(Mockito.any(User.class));

        // Asserting the result
        assertEquals(expectedUser, result, "Expected user deactivation result to match the expected value");
        assertTrue(result.isSuccess(), "Expected result to be successful");
        assertEquals(expectedUser.getValue().getId(), result.getValue().getId(),
                "Expected deactivated user ID to match the expected value");
        assertTrue(result.getValue().isInactive(), "Expected user to be deactivated");
    }

    @Test
    @DisplayName("ReactivateUser should reactivate a user successfully")
    public void testReactivateUser() {
        // Expected user deactivation
        User user = User.of(
                testId,
                "John",
                "Doe",
                "johnDoe@gmail.com",
                "encodedPassword123"
        ).getValue();
        user.deactivate("User requested deactivation");
        Result<User> expectedUser = Result.success(user);

        // Mocking the repository call
        Mockito.when(userQueryRepository.findById(testId))
                .thenReturn(Optional.of(user));
        Mockito.when(userCommandRepository.save(Mockito.any(User.class)))
                .thenAnswer(invocation -> invocation.<User>getArgument(0));

        // Calling the use case method
        var startTime = System.currentTimeMillis();
        Result<User> result = reactivateUser.reactivateById(testId);
        System.out.printf("Total time taken for user reactivation: %dms%n", System.currentTimeMillis() - startTime);

        // Verifying the interaction with the repository
        Mockito.verify(userQueryRepository, Mockito.times(1)).findById(testId);
        Mockito.verify(userCommandRepository, Mockito.times(1)).save(Mockito.any(User.class));

        // Asserting the result
        assertEquals(expectedUser, result, "Expected user reactivation result to match the expected value");
        assertTrue(result.isSuccess(), "Expected result to be successful");
        assertEquals(expectedUser.getValue().getId(), result.getValue().getId(),
                "Expected reactivated user ID to match the expected value");
        assertTrue(result.getValue().isActive(), "Expected user to be reactivated");
    }

    @Test
    @DisplayName("SuspendUser should suspend a user successfully")
    public void testSuspendUser() {
        // Expected user suspension
        User user = User.of(
                testId,
                "John",
                "Doe",
                "johnDoe@gmail.com",
                "encodedPassword123"
        ).getValue();
        user.suspend("User requested deactivation");
        Result<User> expectedUser = Result.success(user);

        // Mocking the repository call
        Mockito.when(userQueryRepository.findById(testId))
                .thenReturn(Optional.of(user));
        Mockito.when(userCommandRepository.save(Mockito.any(User.class)))
                .thenAnswer(invocation -> invocation.<User>getArgument(0));

        // Calling the use case method
        var startTime = System.currentTimeMillis();
        Result<User> result = suspendUser.suspendById(testId, "User requested suspension");
        System.out.printf("Total time taken for user suspension: %dms%n", System.currentTimeMillis() - startTime);

        // Verifying the interaction with the repository
        Mockito.verify(userQueryRepository, Mockito.times(1)).findById(testId);
        Mockito.verify(userCommandRepository, Mockito.times(1)).save(Mockito.any(User.class));

        // Asserting the result
        assertEquals(expectedUser, result, "Expected user suspension result to match the expected value");
        assertTrue(result.isSuccess(), "Expected result to be successful");
        assertEquals(expectedUser.getValue().getId(), result.getValue().getId(),
                "Expected suspended user ID to match the expected value");
        assertTrue(result.getValue().isSuspended(), "Expected user to be suspended");
    }

    @Test
    @DisplayName("UpdateUser should update a user successfully")
    public void testUpdateUser() {

    }
}
