package jpolanco.springbootapp.unit.infrastructure.http;

import jpolanco.springbootapp.event.application.utils.Changes;
import jpolanco.springbootapp.shared.application.PageResult;
import jpolanco.springbootapp.shared.domain.Report;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.domain.UpdateReport;
import jpolanco.springbootapp.shared.domain.utils.DomainError;
import jpolanco.springbootapp.shared.infrastructure.controllers.ResponseHandler;
import jpolanco.springbootapp.shared.infrastructure.errors.BusinessRuleException;
import jpolanco.springbootapp.shared.infrastructure.mappers.PageCreator;
import jpolanco.springbootapp.shared.utils.SuperResult;
import jpolanco.springbootapp.unit.infrastructure.user.repositorry.TestUserFactory;
import jpolanco.springbootapp.user.domain.domain_events.UserEmailChanged;
import jpolanco.springbootapp.user.domain.model.value_objects.User;
import jpolanco.springbootapp.user.infrastructure.adapters.mappers.dto.UserDtoCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;


import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class HandleResponseTest {
    private UserDtoCreator userDtoCreator;

    private User user;
    private Result<User> userResult;
    private SuperResult<User, Report> userSuperResult;
    private Optional<User> userOptional;

    @BeforeEach
    void setUp() {
        userDtoCreator = new UserDtoCreator();

        // Initialize a user result and super result for testing
        userSuperResult = TestUserFactory.generateUserDomain();
        user = userSuperResult.getSuccess();
        userResult = Result.success(userSuperResult.getSuccess());
        userOptional = Optional.of(userSuperResult.getSuccess());
    }

    @Nested
    @DisplayName("Handle Result Tests")
    class HandleResultTests {
        @Test
        @DisplayName("Handle Result with UserDtoCreator")
        void handleResultWithUserDtoCreator() {
            var response = ResponseHandler.handleResult(userResult, userDtoCreator);

            // Assertions can be added here to verify the response
            assertNotNull(response);
            assertEquals(200, response.getStatusCode().value());
            assertNotNull(response.getBody());
            assertEquals(user.getEmail(), response.getBody().email());
            assertEquals(user.getFirstName(), response.getBody().firstName());
            assertEquals(user.getLastName(), response.getBody().lastName());
        }

        @Test
        @DisplayName("Handle Failure Result with UserDtoCreator")
        void handleFailureResultWithUserDtoCreator() {
            Result<User> failureResult = Result.failure(DomainError.INVALID_FORMAT);
            try {
                ResponseHandler.handleResult(failureResult, userDtoCreator);
                fail("Expected BusinessRuleException to be thrown");
            } catch (BusinessRuleException e) {
                assertNotNull(e.getErrors());
                assertEquals(1, e.getErrors().size());
                assertEquals(DomainError.INVALID_FORMAT, e.getErrors().getFirst());
            }
        }
    }

    @Nested
    @DisplayName("Handle Void Result")
    class HandleVoidResultTests {
        @Test
        @DisplayName("Handle Void Result with Success")
        void handleVoidResultWithSuccess() {
            var response = ResponseHandler.handleVoidResult(Result.success(), "Operation successful");

            assertNotNull(response);
            assertEquals(200, response.getStatusCode().value());
            assertNotNull(response.getBody());
            assertEquals("Operation successful", response.getBody().message());
        }

        @Test
        @DisplayName("Handle Void Result with Failure")
        void handleVoidResultWithFailure() {
            try {
                ResponseHandler.handleVoidResult(Result.failure(DomainError.INVALID_FORMAT), "Operation failed");
                fail("Expected BusinessRuleException to be thrown");
            } catch (BusinessRuleException e) {
                assertNotNull(e.getErrors());
                assertEquals(1, e.getErrors().size());
                assertEquals(DomainError.INVALID_FORMAT, e.getErrors().getFirst());
            }
        }
    }

    @Nested
    @DisplayName("Handle Super Result Tests")
    class HandleSuperResultTests {
        @Test
        @DisplayName("Handle Super Result with UserDtoCreator")
        void handleSuperResultWithUserDtoCreator() {
            var response = ResponseHandler.handleSuperResult(userSuperResult, userDtoCreator, HttpStatus.OK);
            assertNotNull(response);
            assertEquals(200, response.getStatusCode().value());
            assertNotNull(response.getBody());
            assertEquals(user.getEmail(), response.getBody().email());
            assertEquals(user.getFirstName(), response.getBody().firstName());
            assertEquals(user.getLastName(), response.getBody().lastName());
        }

        @Test
        @DisplayName("Handle Failure Super Result with UserDtoCreator")
        void handleFailureSuperResultWithUserDtoCreator() {
            SuperResult<User, Report> failureSuperResult = SuperResult.failure(Report.failure(DomainError.INVALID_FORMAT));
            try {
                ResponseHandler.handleSuperResult(failureSuperResult, userDtoCreator, HttpStatus.OK);
                fail("Expected BusinessRuleException to be thrown");
            } catch (BusinessRuleException e) {
                assertNotNull(e.getErrors());
                assertEquals(1, e.getErrors().size());
                assertEquals(DomainError.INVALID_FORMAT, e.getErrors().getFirst());
            }

            failureSuperResult = SuperResult.failure(Report.failure(List.of(
                    DomainError.NULL_VALUE,
                    DomainError.TOO_LONG,
                    DomainError.TOO_SHORT)));
            try {
                ResponseHandler.handleSuperResult(failureSuperResult, userDtoCreator, HttpStatus.OK);
                fail("Expected BusinessRuleException to be thrown");
            } catch (BusinessRuleException e) {
                assertNotNull(e.getErrors());
                assertEquals(3, e.getErrors().size());
                assertTrue(e.getErrors().contains(DomainError.NULL_VALUE));
                assertTrue(e.getErrors().contains(DomainError.TOO_LONG));
                assertTrue(e.getErrors().contains(DomainError.TOO_SHORT));
            }
        }
    }

    @Nested
    @DisplayName("Handle Optional Result Tests")
    class HandleOptionalResultTests {
        @Test
        @DisplayName("Handle Optional with UserDtoCreator")
        void handleOptionalWithUserDtoCreator() {
            var response = ResponseHandler.handleOptional(userOptional, userDtoCreator);
            assertNotNull(response);
            assertEquals(200, response.getStatusCode().value());
            assertNotNull(response.getBody());
            assertEquals(user.getEmail(), response.getBody().email());
            assertEquals(user.getFirstName(), response.getBody().firstName());
            assertEquals(user.getLastName(), response.getBody().lastName());
        }

        @Test
        @DisplayName("Handle Empty Optional with UserDtoCreator")
        void handleEmptyOptionalWithUserDtoCreator() {
            var emptyResponse = ResponseHandler.handleOptional(Optional.empty(), userDtoCreator);
            assertNotNull(emptyResponse);
            assertEquals(404, emptyResponse.getStatusCode().value());
        }
    }

    @Nested
    @DisplayName("Handle List Tests")
    class HandleListTests {
        @Test
        @DisplayName("Handle List with UserDtoCreator")
        void handleListWithUserDtoCreator() {
            var users = TestUserFactory.generateUserDomainList();
            var response = ResponseHandler.handleList(users, userDtoCreator);
            assertNotNull(response);
            assertEquals(200, response.getStatusCode().value());
            assertNotNull(response.getBody());
            assertEquals(users.size(), response.getBody().size());
            assertEquals(users.getFirst().getEmail(), response.getBody().getFirst().email());
        }

        @Test
        @DisplayName("Handle Empty List with UserDtoCreator")
        void handleEmptyListWithUserDtoCreator() {
            var emptyResponse = ResponseHandler.handleList(List.of(), userDtoCreator);
            assertNotNull(emptyResponse);
            assertEquals(204, emptyResponse.getStatusCode().value());
        }
    }

    @Nested
    @DisplayName("Handle Report Tests")
    class HandleReportTests {
        @Test
        @DisplayName("Handle success Report")
        void handleReportWithEventDtoCreator() {
            var report = UpdateReport.success(List.of(new Changes("field1", "oldValue1", "newValue1")),
                    List.of(new UserEmailChanged(UUID.randomUUID(), "oldP", "p")));
            var response = ResponseHandler.handleUpdateReport(report, "Report message");
            assertNotNull(response);
            assertEquals(200, response.getStatusCode().value());
            assertNotNull(response.getBody());
            assertEquals("Report message", response.getBody().message());
            assertEquals(1, response.getBody().changes().size());
            assertEquals("field1", response.getBody().changes().getFirst().field());
            assertEquals("oldValue1", response.getBody().changes().getFirst().before());
            assertEquals("newValue1", response.getBody().changes().getFirst().after());
        }

        @Test
        @DisplayName("Handle failure Report")
        void handleFailureReportWithEventDtoCreator() {
            var failureReport = UpdateReport.failure(DomainError.INVALID_FORMAT);
            try {
                ResponseHandler.handleUpdateReport(failureReport, "Report message");
                fail("Expected BusinessRuleException to be thrown");
            } catch (BusinessRuleException e) {
                assertNotNull(e.getErrors());
                assertEquals(1, e.getErrors().size());
                assertEquals(DomainError.INVALID_FORMAT, e.getErrors().getFirst());
            }
        }
    }

    @Nested
    @DisplayName("Handle collection tests")
    class HandleCollectionTests {
        @Test
        @DisplayName("Handle collection with UserDtoCreator")
        void handleCollectionWithUserDtoCreator() {
            var pageResult = new PageResult<>(
                    TestUserFactory.generateUserDomainList(),
                    10,
                    3,
                    true
            );
            var response = ResponseHandler.handleCollection(pageResult, PageCreator.getInstance(), userDtoCreator);
            assertNotNull(response);
            assertEquals(200, response.getStatusCode().value());
            assertNotNull(response.getBody());
            assertEquals(pageResult.items().size(), response.getBody().content().size());
            assertEquals(pageResult.items().getFirst().getEmail(), response.getBody().content().getFirst().email());
            assertEquals(pageResult.totalElements(), response.getBody().totalElements());
            assertEquals(pageResult.totalPages(), response.getBody().totalPages());
            assertTrue(response.getBody().hasNextPage());
        }

        @Test
        @DisplayName("Handle empty collection with UserDtoCreator")
        void handleEmptyCollectionWithUserDtoCreator() {
            // Create an empty PageResult
            var emptyPageResult = new PageResult<User>(
                    List.of(),
                    0,
                    0,
                    false
            );
            var emptyResponse = ResponseHandler.handleCollection(emptyPageResult, PageCreator.getInstance(), userDtoCreator);
            assertNotNull(emptyResponse);
            assertEquals(204, emptyResponse.getStatusCode().value());
            assertNull(emptyResponse.getBody());
        }
    }

    @Nested
    @DisplayName("Handle Errors Tests")
    class HandleErrorsTests {
        @Test
        @DisplayName("Handle Only one error")
        void handleBusinessRuleException() {
            var response = ResponseHandler.handleError("field", "Invalid input", 422, "Details about the error");
            assertNotNull(response);
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("field", response.getBody().field());
            assertEquals("Invalid input", response.getBody().message());
            assertEquals(422, response.getBody().code());
            assertEquals("Details about the error", response.getBody().details());
        }

        @Test
        @DisplayName("Handle Multiple errors")
        void handleMultipleBusinessRuleException() {
            var response = ResponseHandler.handleErrors(
                    List.of(
                            new DomainError(422, "Invalid input 1"),
                            new DomainError(422, "Invalid input 2")
                    )
            );
            assertNotNull(response);
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(2, response.getBody().size());
            assertEquals("Invalid input 1", response.getBody().getFirst().message());
            assertEquals("Invalid input 2", response.getBody().get(1).message());
            assertEquals(422, response.getBody().getFirst().code());
            assertEquals(422, response.getBody().get(1).code());
        }
    }
}
