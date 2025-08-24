package jpolanco.applicationcore.user.infrastructure.adapters.input.controllers;

import jakarta.validation.Valid;
import jpolanco.applicationcore.user.application.services.interfaces.commands.Login;
import jpolanco.applicationcore.user.application.services.interfaces.commands.Refresh;
import jpolanco.applicationcore.user.application.services.interfaces.commands.RegisterAccount;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request.LoginRequest;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request.RefreshRequest;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request.RegisterRequest;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.response.UserTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterAccount registerAccount;

    private final Login loginService;

    private final Refresh refreshService;

    @PostMapping()
    public ResponseEntity<UserTokenResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserTokenResponse response = registerAccount.registerUser(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserTokenResponse> login(@Valid @RequestBody LoginRequest request) {
        UserTokenResponse response = loginService.loginUser(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<UserTokenResponse> refresh(@RequestBody RefreshRequest request) {
        UserTokenResponse response = refreshService.refresh(request.refreshToken());
        return ResponseEntity.ok(response);
    }
}
