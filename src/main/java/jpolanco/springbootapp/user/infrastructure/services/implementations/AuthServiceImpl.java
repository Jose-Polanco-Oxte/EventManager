package jpolanco.springbootapp.user.infrastructure.services.implementations;

import jpolanco.springbootapp.shared.domain.Report;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.infrastructure.errors.InfrastructureError;
import jpolanco.springbootapp.shared.utils.SuperResult;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserTokenResponse;
import jpolanco.springbootapp.user.infrastructure.services.interfaces.JwtService;
import jpolanco.springbootapp.user.application.uc.unique.CreateUserUC;
import jpolanco.springbootapp.user.application.uc.unique.LoginUC;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.LoginRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.RegisterRequest;
import jpolanco.springbootapp.shared.infrastructure.publisher.DomainEventsPublisher;
import jpolanco.springbootapp.user.infrastructure.services.interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final LoginUC loginUC;
    private final CreateUserUC createUserUc;
    private final JwtService jwtService;
    private final DomainEventsPublisher publisher;

    @Transactional
    @Override
    public Result<UserTokenResponse> login(LoginRequest request) {
        var verifiedUser = loginUC.login(request.email(), request.password());
        if (verifiedUser.isFailure()) {
            return Result.failure(verifiedUser.getError());
        }
        var user = verifiedUser.getValue();
        return jwtService.authenticate(user, request.password());
    }

    @Transactional
    @Override
    public SuperResult<UserTokenResponse, Report> register(RegisterRequest request) {
        SuperResult<User, Report> result = createUserUc.create(request);
        if (result.isFailure()) {
            return SuperResult.failure(result.getFailure());
        }
        var user = result.getSuccess();
        var maybeTokens = jwtService.createTokens(user);
        if (maybeTokens.isFailure()) {
            return SuperResult.failure(Report.failure(maybeTokens.getError()));
        }
        publisher.publishAll(user.pullEvents());
        return SuperResult.success(maybeTokens.getValue());
    }

    @Transactional
    @Override
    public Result<UserTokenResponse> refresh(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Result.failure(InfrastructureError.INVALID_HEADER);
        }
        var refreshToken = authHeader.substring(7);
        var result = jwtService.refreshTokens(refreshToken);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        return Result.success(result.getValue());
    }
}