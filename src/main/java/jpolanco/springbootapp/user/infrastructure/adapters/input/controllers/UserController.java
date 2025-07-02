package jpolanco.springbootapp.user.infrastructure.adapters.input.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jpolanco.springbootapp.shared.infrastructure.dto.response.ChangesResponse;
import jpolanco.springbootapp.shared.infrastructure.dto.request.CommandReasonRequest;
import jpolanco.springbootapp.shared.infrastructure.controllers.ResponseHandler;
import jpolanco.springbootapp.shared.infrastructure.dto.response.CursorPageResponse;
import jpolanco.springbootapp.shared.infrastructure.dto.response.PageResponse;
import jpolanco.springbootapp.shared.infrastructure.dto.response.SimpleResponse;
import jpolanco.springbootapp.shared.infrastructure.mappers.CursorPageCreator;
import jpolanco.springbootapp.shared.infrastructure.mappers.PageCreator;
import jpolanco.springbootapp.shared.utils.OrderField;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.AllFieldsUserUpdate;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserResponse;
import jpolanco.springbootapp.user.infrastructure.adapters.mappers.dto.UserDtoCreator;
import jpolanco.springbootapp.user.infrastructure.components.utils.UserSortField;
import jpolanco.springbootapp.user.infrastructure.services.interfaces.UserCommandService;
import jpolanco.springbootapp.user.infrastructure.services.interfaces.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/users")
public class UserController {
    private final UserQueryService queryService;
    private final UserCommandService commandService;

    @PutMapping("/{userId}")
    public ResponseEntity<ChangesResponse> update(
            @Valid @RequestBody AllFieldsUserUpdate request,
            @PathVariable UUID userId
    ) {
        return ResponseHandler.handleUpdateReport(commandService.update(userId, request),
                "User updated successfully");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID userId) {
        return ResponseHandler.handleOptional(
                queryService.getById(userId),
                UserDtoCreator.getInstance()
        );
    }

    @GetMapping("/{email}/email")
    public ResponseEntity<UserResponse> getUserByEmail(@Email @PathVariable String email) {
        return ResponseHandler.handleOptional(
                queryService.getByEmail(email),
                UserDtoCreator.getInstance()
        );
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<SimpleResponse> delete(
            @Valid @RequestBody CommandReasonRequest request,
            @PathVariable UUID userId
            ) {
        return ResponseHandler.handleVoidResult(
                commandService.deleteById(userId, request.reason()),
                "User deleted successfully"
        );
    }

    @GetMapping("/pages")
    public ResponseEntity<PageResponse<UserResponse>> getUsersByPages(
            @Min(0) @RequestParam(defaultValue = "0", required = false) int page,
            @Min(1) @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "NONE", required = false) UserSortField sortBy,
            @RequestParam(defaultValue = "NONE", required = false) OrderField orderBy
    ) {
        return ResponseHandler.handleCollection(
                queryService.getByPages(page, size, sortBy.getField(), orderBy.getValue()),
                PageCreator.getInstance(), UserDtoCreator.getInstance()
        );
    }

    @GetMapping("/cursor")
    public ResponseEntity<CursorPageResponse<UserResponse, UUID>> getUsersByCursor(
            @RequestParam(required = false) UUID last,
            @Min(1) @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "NONE", required = false) UserSortField sortBy,
            @RequestParam(defaultValue = "NONE", required = false) OrderField orderBy
    ) {
        return ResponseHandler.handleCollection(
                queryService.getByCursor(last, size, sortBy.getField(), orderBy.getValue()),
                CursorPageCreator.getInstance(), UserDtoCreator.getInstance()
        );
    }

    @PutMapping("/{userId}/deactivate")
    public ResponseEntity<SimpleResponse> deactivateUser(
            @Valid @RequestBody CommandReasonRequest request,
            @PathVariable UUID userId
    ) {
        return ResponseHandler.handleVoidResult(
                commandService.deactivateById(userId, request.reason()),
                "User deactivated successfully"
        );
    }

    @PutMapping("/{userId}/reactivate")
    public ResponseEntity<SimpleResponse> reactivateUser(
            @PathVariable UUID userId
    ) {
        return ResponseHandler.handleVoidResult(
                commandService.reactivateById(userId),
                "User reactivated successfully"
        );
    }

    @PutMapping("/{userId}/suspend")
    public ResponseEntity<SimpleResponse> suspendUser(
            @Valid @RequestBody CommandReasonRequest request,
            @PathVariable UUID userId
    ) {
        return ResponseHandler.handleVoidResult(
                commandService.suspendById(userId, request.reason()),
                "User suspended successfully"
        );
    }
}
