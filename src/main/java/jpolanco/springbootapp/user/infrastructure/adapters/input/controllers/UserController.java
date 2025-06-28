package jpolanco.springbootapp.user.infrastructure.adapters.input.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jpolanco.springbootapp.shared.infrastructure.dto.response.ChangesResponse;
import jpolanco.springbootapp.shared.infrastructure.dto.request.CommandReasonRequest;
import jpolanco.springbootapp.event.infrastructure.adapters.input.validations.annotations.ValidUUID;
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
            @ValidUUID @PathVariable String userId
    ) {
        return ResponseHandler.handleReport(commandService.update(request, userId),
                "User updated successfully");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@ValidUUID @PathVariable String userId) {
        return ResponseHandler.handleOptional(
                queryService.getById(userId),
                UserDtoCreator.getInstance()
        );
    }

    @GetMapping("/{email}/email")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        return ResponseHandler.handleOptional(
                queryService.getByEmail(email),
                UserDtoCreator.getInstance()
        );
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<SimpleResponse> delete(
            @PathVariable @ValidUUID String userId,
            @Valid @RequestBody CommandReasonRequest request
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
    public ResponseEntity<CursorPageResponse<UserResponse, String>> getUsersByCursor(
            @RequestParam(defaultValue = "NONE", required = false) String cursor,
            @Min(1) @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "NONE", required = false) UserSortField sortBy,
            @RequestParam(defaultValue = "NONE", required = false) OrderField orderBy
    ) {
        return ResponseHandler.handleCollection(
                queryService.getByCursor(cursor, size, sortBy.getField(), orderBy.getValue()),
                CursorPageCreator.getInstance(), UserDtoCreator.getInstance()
        );
    }

    @PutMapping("/{userId}/deactivate")
    public ResponseEntity<SimpleResponse> deactivateUser(
            @ValidUUID @PathVariable String userId,
            @Valid @RequestBody CommandReasonRequest request
    ) {
        return ResponseHandler.handleVoidResult(
                commandService.deactivateById(userId, request.reason()),
                "User deactivated successfully"
        );
    }

    @PutMapping("/{userId}/reactivate")
    public ResponseEntity<SimpleResponse> reactivateUser(
            @ValidUUID @PathVariable String userId
    ) {
        return ResponseHandler.handleVoidResult(
                commandService.reactivateById(userId),
                "User reactivated successfully"
        );
    }
}
