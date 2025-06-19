package jpolanco.springbootapp.user.infrastructure.adapters.input.controllers;

import jpolanco.springbootapp.user.infrastructure.services.interfaces.SearchUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users/search")
public class SearchUserController {
    private final SearchUserService searchUserService;

    @GetMapping("/by-name")
    public ResponseEntity<Object> searchByName(
            @RequestParam() String name,
            @RequestParam(defaultValue = "10", required = false) int size
    ) {
        size = Math.max(size, 1);
        var users = searchUserService.searchUsersByName(name, size);
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/by-email")
    public ResponseEntity<Object> searchByEmail(
            @RequestParam String email,
            @RequestParam(defaultValue = "10", required = false) int size
    ) {
        size = Math.max(size, 1);
        var users = searchUserService.searchUsersByEmail(email, size);
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }
}
