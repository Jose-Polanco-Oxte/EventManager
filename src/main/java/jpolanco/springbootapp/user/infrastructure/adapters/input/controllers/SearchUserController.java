package jpolanco.springbootapp.user.infrastructure.adapters.input.controllers;

import jakarta.validation.constraints.Min;
import jpolanco.springbootapp.shared.infrastructure.controllers.ResponseHandler;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserResponse;
import jpolanco.springbootapp.user.infrastructure.adapters.mappers.dto.UserDtoCreator;
import jpolanco.springbootapp.user.infrastructure.services.interfaces.SearchUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/users/search")
public class SearchUserController {
    private final SearchUserService searchUserService;

    @GetMapping("/name")
    public ResponseEntity<List<UserResponse>> searchByName(
            @RequestParam() String query,
            @Min(1) @RequestParam(defaultValue = "10", required = false) int size
    ) {
        return ResponseHandler.handleList(
                searchUserService.searchByName(query, size),
                UserDtoCreator.getInstance());
    }

    @GetMapping("/email")
    public ResponseEntity<List<UserResponse>> searchByEmail(
            @RequestParam String email,
            @Min(1) @RequestParam(defaultValue = "10", required = false) int size
    ) {
        return ResponseHandler.handleList(
                searchUserService.searchByEmail(email, size),
                UserDtoCreator.getInstance());
    }
}
