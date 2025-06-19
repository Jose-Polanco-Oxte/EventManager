package jpolanco.springbootapp.user.infrastructure.adapters.input.controllers;

import jakarta.validation.Valid;
import jpolanco.springbootapp.shared.utils.OrderField;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.AnyUserUpdateRequest;
import jpolanco.springbootapp.user.infrastructure.components.utils.UserSortField;
import jpolanco.springbootapp.user.infrastructure.services.interfaces.UserCommandService;
import jpolanco.springbootapp.user.infrastructure.services.interfaces.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserQueryService queryService;
    private final UserCommandService commandService;

    @PostMapping("/update/{userId}")
    public ResponseEntity<Object> update(@Valid @RequestBody AnyUserUpdateRequest request,
                                          @PathVariable String userId) {
        var response = commandService.updateUser(request, userId);
        if (response.isFailure()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getMessage());
        }
        return ResponseEntity.ok(response.getValue());
    }

    @GetMapping("get/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable String userId) {
        var response = queryService.getUserById(userId);
        if (response.isFailure()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getMessage());
        }
        return ResponseEntity.ok(response.getValue());
    }

    @GetMapping("get-by-email/{email}")
    public ResponseEntity<Object> getUserByEmail(@PathVariable String email) {
        var response = queryService.getUserByEmail(email);
        if (response.isFailure()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getMessage());
        }
        return ResponseEntity.ok(response.getValue());
    }

    @DeleteMapping("delete-by-id/{userId}")
    public ResponseEntity<Object> delete(@PathVariable String userId) {
        var response = commandService.deleteUserById(userId);
        if (response.isFailure()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getMessage());
        }
        return ResponseEntity.ok(response.getValue());
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllUsers() {
        var response = queryService.getAll();
        if (response.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/by-pages")
    public ResponseEntity<Object> getUsersByPages(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "NONE", required = false) UserSortField sortBy,
            @RequestParam(defaultValue = "NONE", required = false) OrderField orderBy
    ) {
        page = Math.max(page, 0);
        size = Math.max(size, 1);
        var response = queryService.getUsers(page, size, sortBy.getField(), orderBy.getValue());
        if (response.content().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/by-cursor")
    public ResponseEntity<Object> getUsersByCursor(
            @RequestParam(defaultValue = "NONE", required = false) String cursor,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "NONE", required = false) UserSortField sortBy,
            @RequestParam(defaultValue = "NONE", required = false) OrderField orderBy
    ) {
        size = Math.max(size, 1);
        var response = queryService.getUsers(cursor, size, sortBy.getField(), orderBy.getValue());
        if (response.items().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }
}
