package jpolanco.springbootapp.user.infrastructure.adapters.input.controllers;

import jakarta.validation.Valid;
import jpolanco.springbootapp.shared.infrastructure.controllers.ResponseHandler;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.LoginRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.RegisterRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserTokenResponse;
import jpolanco.springbootapp.user.infrastructure.adapters.mappers.dto.TokenDtoCreator;
import jpolanco.springbootapp.user.infrastructure.services.interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<UserTokenResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseHandler.handleEither(authService.register(request),
                TokenDtoCreator.getInstance(), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserTokenResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseHandler.handleResult(authService.login(request), TokenDtoCreator.getInstance());
    }

    @PostMapping("/refresh")
    public ResponseEntity<UserTokenResponse> refresh(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return ResponseHandler.handleResult(authService.refresh(authorizationHeader), TokenDtoCreator.getInstance());
    }
}
