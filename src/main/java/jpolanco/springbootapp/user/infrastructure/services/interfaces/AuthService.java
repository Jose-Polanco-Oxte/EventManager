package jpolanco.springbootapp.user.infrastructure.services.interfaces;
import jpolanco.springbootapp.shared.domain.utils.Error;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.utils.Either;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.LoginRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.RegisterRequest;

import java.util.List;
import java.util.Map;

public interface AuthService {
    Result<Map<String, String>> login(LoginRequest request);
    Either<Map<String, String>, List<Error>> register(RegisterRequest request);
    Result<Map<String, String>> refresh(String authHeader);
}
