package jpolanco.springbootapp.user.infrastructure.services.interfaces;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.LoginRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.RegisterRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserTokenResponse;

public interface AuthService {
    Result<UserTokenResponse> login(LoginRequest request);
    Result<UserTokenResponse> register(RegisterRequest request);
    Result<UserTokenResponse> refresh(String authHeader);
}
