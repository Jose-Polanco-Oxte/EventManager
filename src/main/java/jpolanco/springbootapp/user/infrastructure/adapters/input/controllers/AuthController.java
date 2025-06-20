package jpolanco.springbootapp.user.infrastructure.adapters.input.controllers;

import jakarta.validation.Valid;
import jpolanco.springbootapp.shared.infrastructure.controllers.ResponseHandler;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.LoginRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.RegisterRequest;
import jpolanco.springbootapp.user.infrastructure.services.interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterRequest request) {
        var commandResult = authService.register(request);
        if (commandResult.isFailure()) {
            return ResponseHandler.error(commandResult.getMessage(), commandResult.getErrorCode());
        }
        return ResponseHandler.ok(commandResult);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequest request) {
        var commandResult = authService.login(request);
        if (commandResult.isFailure()) {
            return ResponseHandler.error(commandResult.getMessage(), commandResult.getErrorCode());
        }
        return ResponseHandler.ok(commandResult);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Object> refresh(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        var commandResult = authService.refresh(authorizationHeader);
        if (commandResult.isFailure()) {
            return ResponseHandler.error(commandResult.getMessage(), commandResult.getErrorCode());
        }
        return ResponseHandler.ok(commandResult);
    }
}
