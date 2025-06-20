package jpolanco.springbootapp.user.infrastructure.adapters.input.controllers;

import jakarta.validation.constraints.Min;
import jpolanco.springbootapp.shared.infrastructure.controllers.ResponseHandler;
import jpolanco.springbootapp.user.infrastructure.services.interfaces.SearchUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/users/search")
public class SearchUserController {
    private final SearchUserService searchUserService;

    @GetMapping("/name")
    public ResponseEntity<Object> searchByName(
            @RequestParam() String query,
            @Min(1) @RequestParam(defaultValue = "10", required = false) int size
    ) {
        var result = searchUserService.searchByName(query, size);
        if (result.isEmpty()) {
            return ResponseHandler.noContent();
        }
        return ResponseHandler.ok(result);
    }

    @GetMapping("/email")
    public ResponseEntity<Object> searchByEmail(
            @RequestParam String email,
            @Min(1) @RequestParam(defaultValue = "10", required = false) int size
    ) {
        var result = searchUserService.searchByEmail(email, size);
        if (result.isEmpty()) {
            return ResponseHandler.noContent();
        }
        return ResponseHandler.ok(result);
    }
}
