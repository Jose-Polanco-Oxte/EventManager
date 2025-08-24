package jpolanco.applicationcore.user.infrastructure.adapters.input.controllers;

import jakarta.validation.Valid;
import jpolanco.applicationcore.config.auth.MyUserDetails;
import jpolanco.applicationcore.shared.infrastructure.dto.request.CommandReason;
import jpolanco.applicationcore.user.application.services.interfaces.commands.DeactivateAccount;
import jpolanco.applicationcore.user.application.services.interfaces.commands.DeleteAccount;
import jpolanco.applicationcore.user.application.services.interfaces.commands.ProfileChanges;
import jpolanco.applicationcore.user.application.services.interfaces.commands.ReactivateAccount;
import jpolanco.applicationcore.user.application.services.interfaces.queries.FindUserById;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request.ChangeEmailRequest;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request.ChangeNameRequest;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request.ChangePasswordRequest;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.response.UserResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final DeactivateAccount deactivateAccount;

    private final ReactivateAccount reactivateAccount;

    private final DeleteAccount deleteAccount;

    private final ProfileChanges profileChanges;
    
    private final FindUserById findUserById;

    public AccountController(
            DeactivateAccount deactivateAccount,
            ReactivateAccount reactivateAccount,
            DeleteAccount deleteAccount,
            ProfileChanges profileChanges,
            @Qualifier("findUserByIdDefault") FindUserById findUserById
    ) {
        this.deactivateAccount = deactivateAccount;
        this.reactivateAccount = reactivateAccount;
        this.deleteAccount = deleteAccount;
        this.profileChanges = profileChanges;
        this.findUserById = findUserById;
    }


    @GetMapping()
    public ResponseEntity<UserResponse> getMe(@AuthenticationPrincipal MyUserDetails userDetails) {
        Optional<UserResponse> maybeUser = findUserById.find(userDetails.getId());
        return maybeUser
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }

    @PutMapping("/name")
    public ResponseEntity<UserResponse> changeMyName(
            @AuthenticationPrincipal MyUserDetails userDetails,
            @Valid @RequestBody ChangeNameRequest request
    ) {
        UserResponse response = profileChanges.changeName(userDetails.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/email")
    public ResponseEntity<UserResponse> changeMyEmail(
            @AuthenticationPrincipal MyUserDetails userDetails,
            @Valid @RequestBody ChangeEmailRequest request
    ) {
        UserResponse response = profileChanges.changeEmail(userDetails.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/password")
    public ResponseEntity<UserResponse> updatePassword(
            @AuthenticationPrincipal MyUserDetails userDetails,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        UserResponse response = profileChanges.changePassword(userDetails.getId(), request);
        return ResponseEntity.ok(response);
    }

    // TODO: Implement profile picture update logic
    @PutMapping("/picture")
    public ResponseEntity<?> updateProfilePicture(
            @AuthenticationPrincipal MyUserDetails userDetails,
            @RequestParam("picture") String pictureUrl
    ) {
        return ResponseEntity.ok("This feature is not implemented yet.");
    }

    @PutMapping("/deactivate")
    public ResponseEntity<UserResponse> deactivateAccount(
            @AuthenticationPrincipal MyUserDetails userDetails,
            @Valid @RequestBody CommandReason request
    ) {
        UserResponse response = deactivateAccount.deactivateMainById(userDetails.getId(), request.reason());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/reactivate")
    public ResponseEntity<UserResponse> reactivateAccount(
            @AuthenticationPrincipal MyUserDetails userDetails
    ) {
        UserResponse response = reactivateAccount.reactivateMainById(userDetails.getId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteAccount(
            @AuthenticationPrincipal MyUserDetails userDetails,
            @Valid @RequestBody CommandReason request
    ) {
        deleteAccount.deleteMainById(userDetails.getId(), request.reason());
        return ResponseEntity.noContent().build();
    }
}
