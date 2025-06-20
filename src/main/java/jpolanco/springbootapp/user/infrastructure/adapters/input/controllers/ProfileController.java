package jpolanco.springbootapp.user.infrastructure.adapters.input.controllers;
import jakarta.validation.Valid;
import jpolanco.springbootapp.config.auth.MyUserDetails;
import jpolanco.springbootapp.shared.infrastructure.controllers.ResponseHandler;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.UpdateEmailRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.UpdateNameRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.UpdatePasswordRequest;
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
    public ResponseEntity<Object> getMe(@AuthenticationPrincipal MyUserDetails userDetails) {
        var response = queryService.getById(userDetails.getId());
        return response.map(ResponseHandler::ok).orElseGet(ResponseHandler::notFound);
    }

    @PutMapping("/name")
    public ResponseEntity<Object> changeMyName(
            @AuthenticationPrincipal MyUserDetails userDetails,
            @Valid @RequestBody UpdateNameRequest request
    ) {
        var commandResult = profileService.changeName(userDetails.getId(), request);
        if (commandResult.isFailure()) {
            return ResponseHandler.error(commandResult.getMessage(), commandResult.getErrorCode());
        }
        return ResponseHandler.ok("Name updated successfully");
    }

    @PutMapping("/email")
    public ResponseEntity<Object> changeMyEmail(
            @AuthenticationPrincipal MyUserDetails userDetails,
            @Valid @RequestBody UpdateEmailRequest request
    ) {
        var commandResult = profileService.changeEmail(userDetails.getId(), request);
        if (commandResult.isFailure()) {
            return ResponseHandler.error(commandResult.getMessage(), commandResult.getErrorCode());
        }
        return ResponseHandler.ok("Email updated successfully");
    }
    @PutMapping("/password")
    public ResponseEntity<Object> updatePassword(
            @AuthenticationPrincipal MyUserDetails userDetails,
            @Valid @RequestBody UpdatePasswordRequest request
    ) {
        var commandResult = profileService.changePassword(userDetails.getId(), request);
        if (commandResult.isFailure()) {
            return ResponseHandler.error(commandResult.getMessage(), commandResult.getErrorCode());
        }
        return ResponseHandler.ok("Password updated successfully");
    }

    @DeleteMapping()
    public ResponseEntity<Object> deleteAccount(@AuthenticationPrincipal MyUserDetails userDetails) {
        var commandResult = profileService.delete(userDetails.getId());
        if (commandResult.isFailure()) {
            return ResponseHandler.error(commandResult.getMessage(), commandResult.getErrorCode());
        }
        return ResponseHandler.ok("Account deleted successfully");
    }
}
