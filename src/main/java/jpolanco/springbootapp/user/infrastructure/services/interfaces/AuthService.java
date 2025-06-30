package jpolanco.springbootapp.user.infrastructure.services.interfaces;
import jpolanco.springbootapp.shared.domain.Report;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.utils.SuperResult;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.LoginRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.RegisterRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserTokenResponse;

import java.util.Map;

public interface AuthService {
    Result<UserTokenResponse> login(LoginRequest request);
    SuperResult<UserTokenResponse, Report> register(RegisterRequest request);
    Result<UserTokenResponse> refresh(String authHeader);
}
