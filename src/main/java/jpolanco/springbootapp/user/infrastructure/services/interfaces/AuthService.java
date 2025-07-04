package jpolanco.springbootapp.user.infrastructure.services.interfaces;
import jpolanco.springbootapp.shared.utils.results.Report;
import jpolanco.springbootapp.shared.utils.results.Result;
import jpolanco.springbootapp.shared.utils.results.SuperResult;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.LoginRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.RegisterRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserTokenResponse;

public interface AuthService {
    Result<UserTokenResponse> login(LoginRequest request);
    SuperResult<UserTokenResponse, Report> register(RegisterRequest request);
    Result<UserTokenResponse> refresh(String authHeader);
}
