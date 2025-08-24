package jpolanco.applicationcore.user.infrastructure.adapters.input.controllers;

import jakarta.validation.Valid;
import jpolanco.applicationcore.config.auth.MyUserDetails;
import jpolanco.applicationcore.shared.application.pageable.Cursored;
import jpolanco.applicationcore.shared.application.pageable.Paged;
import jpolanco.applicationcore.shared.infrastructure.dto.request.CommandReason;
import jpolanco.applicationcore.user.application.services.interfaces.commands.*;
import jpolanco.applicationcore.user.application.services.interfaces.queries.FindUserByEmail;
import jpolanco.applicationcore.user.application.services.interfaces.queries.FindUserById;
import jpolanco.applicationcore.user.application.services.interfaces.queries.GetUsersByAdmin;
import jpolanco.applicationcore.user.application.services.interfaces.queries.SearchUserService;
import jpolanco.applicationcore.user.domain.dto.UpdateAllUser;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request.AdminUserFilterRequest;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request.CursorRequest;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request.PagesRequest;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request.SearchQueryRequest;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.response.UserResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/admin/users")
public class AdminController {

    private final DeactivateAccount deactivateAccount;

    private final ReactivateAccount reactivateAccount;

    private final SuspendAccount suspendAccount;

    private final DeleteAccount deleteAccount;

    private final UpdateAccount updateAccount;

    private final FindUserById findUserById;

    private final FindUserByEmail findUserByEmail;

    private final SearchUserService searchUserService;
    
    private final GetUsersByAdmin getUsersByAdmin;

    public AdminController(
            DeactivateAccount deactivateAccount,
            ReactivateAccount reactivateAccount,
            SuspendAccount suspendAccount,
            DeleteAccount deleteAccount,
            UpdateAccount updateAccount,
            @Qualifier("findUserByIdAdmin") FindUserById findUserById,
            @Qualifier("findUserByEmailAdmin") FindUserByEmail findUserByEmail,
            @Qualifier("searchUserServiceAdmin") SearchUserService searchUserService,
            GetUsersByAdmin getUsersByAdmin
    ) {
        this.deactivateAccount = deactivateAccount;
        this.reactivateAccount = reactivateAccount;
        this.suspendAccount = suspendAccount;
        this.deleteAccount = deleteAccount;
        this.updateAccount = updateAccount;
        this.findUserById = findUserById;
        this.findUserByEmail = findUserByEmail;
        this.searchUserService = searchUserService;
        this.getUsersByAdmin = getUsersByAdmin;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> findUser(@PathVariable UUID userId) {
        Optional<UserResponse> response = findUserById.find(userId);
        return response.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{email}/email")
    public ResponseEntity<UserResponse> findUserByEmail(@PathVariable String email) {
        Optional<UserResponse> response = findUserByEmail.find(email);
        return response.map(ResponseEntity::ok)
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
    public ResponseEntity<Paged<UserResponse>> getUsersByPagesAndCursor(
            @ModelAttribute PagesRequest request,
            @RequestBody(required = false) @Valid AdminUserFilterRequest filters
    ) {
        Paged<UserResponse> responses = getUsersByAdmin.getByPages(request, filters);
        if (responses.getContent().isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(responses);
        }
    }

    @GetMapping("/cursor")
    public ResponseEntity<Cursored<UserResponse>> getUsersByCursor(
            @ModelAttribute @Valid CursorRequest request,
            @RequestBody(required = false) @Valid AdminUserFilterRequest filters
    ) {
        Cursored<UserResponse> responses = getUsersByAdmin.getByCursor(request.cursor().orElse(null), request, filters);
        if (responses.getContent().isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(responses);
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            @RequestBody UpdateAllUser request,
            @PathVariable UUID userId
    ) {
        UserResponse response = updateAccount.setChanges(myUserDetails.getId(), userId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{userId}/deactivate")
    public ResponseEntity<UserResponse> deactivateUser(
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            @Valid @RequestBody CommandReason request,
            @PathVariable UUID userId
    ) {
        UserResponse response = deactivateAccount.deactivateById(myUserDetails.getId(), userId, request.reason());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{userId}/reactivate")
    public ResponseEntity<UserResponse> reactivateUser(
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            @PathVariable UUID userId
    ) {
        UserResponse response = reactivateAccount.reactivateById(myUserDetails.getId(), userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{userId}/suspend")
    public ResponseEntity<UserResponse> suspendUser(
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            @Valid @RequestBody CommandReason request,
            @PathVariable UUID userId
    ) {
        UserResponse response = suspendAccount.suspendById(myUserDetails.getId(), userId, request.reason());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            @Valid @RequestBody CommandReason request,
            @PathVariable UUID userId
    ) {
        deleteAccount.deleteById(myUserDetails.getId(), userId, request.reason());
        return ResponseEntity.noContent().build();
    }
}
