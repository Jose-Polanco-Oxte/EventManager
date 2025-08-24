package jpolanco.applicationcore.user.infrastructure.adapters.input.controllers;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jpolanco.applicationcore.shared.application.pageable.Cursored;
import jpolanco.applicationcore.shared.application.pageable.Paged;
import jpolanco.applicationcore.user.application.services.interfaces.queries.FindUserByEmail;
import jpolanco.applicationcore.user.application.services.interfaces.queries.FindUserById;
import jpolanco.applicationcore.user.application.services.interfaces.queries.GetUsers;
import jpolanco.applicationcore.user.application.services.interfaces.queries.SearchUserService;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request.CursorRequest;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request.DefaultUserFilterRequest;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request.PagesRequest;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request.SearchQueryRequest;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.response.UserResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final FindUserById findUserById;

    private final FindUserByEmail findUserByEmail;

    private final SearchUserService searchUserService;
    
    private final GetUsers pageableService;

    public UserController(
            @Qualifier("findUserByIdDefault") FindUserById findUserById,
            @Qualifier("findUserByEmailDefault") FindUserByEmail findUserByEmail,
            @Qualifier("searchUserServiceDefault") SearchUserService searchUserService,
            @Qualifier("getUsersDefault") GetUsers pageableService
    ) {
        this.findUserById = findUserById;
        this.findUserByEmail = findUserByEmail;
        this.searchUserService = searchUserService;
        this.pageableService = pageableService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> findUser(@PathVariable UUID userId) {
        Optional<UserResponse> userResponse = findUserById.find(userId);
        return userResponse
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{email}/email")
    public ResponseEntity<UserResponse> findUserByEmail(@Email @PathVariable String email) {
        Optional<UserResponse> userResponse = findUserByEmail.find(email);
        return userResponse
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/search/name")
    public ResponseEntity<List<UserResponse>> searchByName(SearchQueryRequest request) {
        List<UserResponse> responses = searchUserService.searchByName(request);
        if (responses.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(responses);
        }
    }

    @GetMapping("/search/email")
    public ResponseEntity<List<UserResponse>> searchByEmail(SearchQueryRequest request) {
        List<UserResponse> responses = searchUserService.searchByEmail(request);
        if (responses.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(responses);
        }
    }

    @GetMapping("/pages")
    public ResponseEntity<Paged<UserResponse>> getByPages(
            @ModelAttribute PagesRequest request,
            @RequestBody(required = false) @Valid DefaultUserFilterRequest filters
    ) {
        Paged<UserResponse> response = pageableService.getByPages(request, filters);
        if (response.getContent().isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/cursor")
    public ResponseEntity<Cursored<UserResponse>> getByCursor(
            @ModelAttribute @Valid CursorRequest request,
            @RequestBody(required = false) @Valid DefaultUserFilterRequest filters
    ) {
        Cursored<UserResponse> response = pageableService.getByCursor(request.cursor().orElse(null), request, filters);
        if (response.getContent().isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(response);
        }
    }
}
