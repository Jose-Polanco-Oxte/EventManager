package jpolanco.springbootapp.user.infrastructure.adapters.input.controllers;

import jakarta.validation.Valid;
import jpolanco.springbootapp.config.auth.MyUserDetails;
import jpolanco.springbootapp.shared.infrastructure.dto.request.CommandReasonRequest;
import jpolanco.springbootapp.shared.infrastructure.controllers.ResponseHandler;
import jpolanco.springbootapp.shared.infrastructure.dto.response.SimpleResponse;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.ChangeEmailRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.ChangeNameRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.ChangePasswordRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserResponse;
import jpolanco.springbootapp.user.infrastructure.adapters.mappers.dto.UserDtoCreator;
import jpolanco.springbootapp.user.infrastructure.services.interfaces.ProfileService;
import jpolanco.springbootapp.user.infrastructure.services.interfaces.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;
    private final UserQueryService queryService;

    @GetMapping()
    public ResponseEntity<UserResponse> getMe(@AuthenticationPrincipal MyUserDetails userDetails) {
        return ResponseHandler.handleOptional(queryService.getById(userDetails.getId()),
                UserDtoCreator.getInstance());
    }

    @PutMapping("/name")
    public ResponseEntity<?> changeMyName(
            @AuthenticationPrincipal MyUserDetails userDetails,
            @Valid @RequestBody ChangeNameRequest request
    ) {
        return ResponseHandler.handleReport(
                profileService.changeName(userDetails.getId(), request),
                "Name updated successfully");
    }

    @PutMapping("/email")
    public ResponseEntity<SimpleResponse> changeMyEmail(
            @AuthenticationPrincipal MyUserDetails userDetails,
            @Valid @RequestBody ChangeEmailRequest request
    ) {
        return ResponseHandler.handleVoidResult(profileService.changeEmail(userDetails.getId(), request), "Email updated successfully");
    }

    @PutMapping("/password")
    public ResponseEntity<SimpleResponse> updatePassword(
            @AuthenticationPrincipal MyUserDetails userDetails,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        return ResponseHandler.handleVoidResult(
                profileService.changePassword(userDetails.getId(), request),
                "Password updated successfully"
        );
    }

    // TODO: Implement profile picture update logic
    @PutMapping("/picture")
    public ResponseEntity<?> updateProfilePicture(
            @AuthenticationPrincipal MyUserDetails userDetails,
            @RequestParam("picture") String pictureUrl
    ) {
        return null;
    }

    @PutMapping("/deactivate")
    public ResponseEntity<SimpleResponse> deactivateAccount(
            @AuthenticationPrincipal MyUserDetails userDetails,
            @Valid @RequestBody CommandReasonRequest request
    ) {
        return ResponseHandler.handleVoidResult(
                profileService.deactivate(userDetails.getId(), request.reason()),
                "Account deactivated successfully"
        );
    }

    @PutMapping("/reactivate")
    public ResponseEntity<SimpleResponse> reactivateAccount(
            @AuthenticationPrincipal MyUserDetails userDetails
    ) {
        return ResponseHandler.handleVoidResult(
                profileService.reactivate(userDetails.getId()),
                "Account reactivated successfully"
        );
    }

    @DeleteMapping()
    public ResponseEntity<SimpleResponse> deleteAccount(
            @AuthenticationPrincipal MyUserDetails userDetails,
            @Valid @RequestBody CommandReasonRequest request
            ) {
        return ResponseHandler.handleVoidResult(
                profileService.delete(userDetails.getId(), request.reason()),
                "Account deleted successfully"
        );
    }
}
