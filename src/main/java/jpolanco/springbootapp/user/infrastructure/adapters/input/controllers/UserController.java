package jpolanco.springbootapp.user.infrastructure.adapters.input.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jpolanco.springbootapp.shared.infrastructure.dto.CommandReasonRequest;
import jpolanco.springbootapp.event.infrastructure.adapters.input.validations.annotations.ValidUUID;
import jpolanco.springbootapp.shared.infrastructure.controllers.ResponseHandler;
import jpolanco.springbootapp.shared.utils.OrderField;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.AnyUserUpdateRequest;
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
    public ResponseEntity<Object> update(
            @Valid @RequestBody AnyUserUpdateRequest request,
            @ValidUUID @PathVariable String userId
    ) {
        var commandREsult = commandService.update(request, userId);
        if (commandREsult.isFailure()) {
//            return ResponseHandler.error(
//                    commandREsult.getMessage(),
//                    commandREsult.getErrorCode()
//            );
        }
        return ResponseHandler.ok("User updated successfully");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@ValidUUID @PathVariable String userId) {
        var response = queryService.getById(userId);
        return response.map(ResponseHandler::ok).orElseGet(ResponseHandler::notFound);
    }

    @GetMapping("/{email}/email")
    public ResponseEntity<Object> getUserByEmail(@PathVariable String email) {
        var response = queryService.getByEmail(email);
        return response.map(ResponseHandler::ok).orElseGet(ResponseHandler::notFound);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delete(
            @PathVariable @ValidUUID String userId,
            @Valid @RequestBody CommandReasonRequest request
            ) {
        var commandResult = commandService.deleteById(userId, request.reason());
        if (commandResult.isFailure()) {
            return ResponseHandler.error(
                    commandResult.getMessage(),
                    commandResult.getErrorCode()
            );
        }
        return ResponseHandler.ok("User deleted successfully");
    }

    @GetMapping("/pages")
    public ResponseEntity<Object> getUsersByPages(
            @Min(0) @RequestParam(defaultValue = "0", required = false) int page,
            @Min(1) @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "NONE", required = false) UserSortField sortBy,
            @RequestParam(defaultValue = "NONE", required = false) OrderField orderBy
    ) {
        var users = queryService.get(page, size, sortBy.getField(), orderBy.getValue());
        if (users.content().isEmpty()) {
            return ResponseHandler.noContent();
        }
        return ResponseHandler.ok(users);
    }

    @GetMapping("/cursor")
    public ResponseEntity<Object> getUsersByCursor(
            @RequestParam(defaultValue = "NONE", required = false) String cursor,
            @Min(1) @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "NONE", required = false) UserSortField sortBy,
            @RequestParam(defaultValue = "NONE", required = false) OrderField orderBy
    ) {
        var users = queryService.get(cursor, size, sortBy.getField(), orderBy.getValue());
        if (users.items().isEmpty()) {
            return ResponseHandler.noContent();
        }
        return ResponseHandler.ok(users);
    }

    @PutMapping("/{userId}/deactivate")
    public ResponseEntity<Object> deactivateUser(
            @ValidUUID @PathVariable String userId,
            @Valid @RequestBody CommandReasonRequest request
    ) {
        var commandResult = commandService.deactivateById(userId, request.reason());
        if (commandResult.isFailure()) {
            return ResponseHandler.error(
                    commandResult.getMessage(),
                    commandResult.getErrorCode()
            );
        }
        return ResponseHandler.ok("User deactivated successfully");
    }

    @PutMapping("/{userId}/reactivate")
    public ResponseEntity<Object> reactivateUser(
            @ValidUUID @PathVariable String userId
    ) {
        var commandResult = commandService.reactivateById(userId);
        if (commandResult.isFailure()) {
            return ResponseHandler.error(
                    commandResult.getMessage(),
                    commandResult.getErrorCode()
            );
        }
        return ResponseHandler.ok("User reactivated successfully");
    }
}
