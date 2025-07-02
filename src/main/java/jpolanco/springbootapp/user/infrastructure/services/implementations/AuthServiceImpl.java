package jpolanco.springbootapp.user.infrastructure.services.implementations;

import jpolanco.springbootapp.shared.application.AppError;
import jpolanco.springbootapp.shared.domain.CreationReport;
import jpolanco.springbootapp.shared.domain.Report;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.utils.Pair;
import jpolanco.springbootapp.shared.utils.SuperResult;
import jpolanco.springbootapp.user.application.uc.derived.RegisterUserUC;
import jpolanco.springbootapp.user.application.uc.unique.RefreshTokenUC;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserTokenResponse;
import jpolanco.springbootapp.user.application.uc.unique.LoginUC;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.LoginRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.RegisterRequest;
import jpolanco.springbootapp.shared.infrastructure.publisher.DomainEventsPublisher;
import jpolanco.springbootapp.user.infrastructure.services.interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final LoginUC loginUC;
    private final RegisterUserUC registerUserUC;
    private final RefreshTokenUC refreshTokenUC;
    private final DomainEventsPublisher publisher;

    @Transactional
    @Override
    public Result<UserTokenResponse> login(LoginRequest request) {
        return loginUC.login(request);
    }

    @Transactional
    @Override
    public SuperResult<UserTokenResponse, Report> register(RegisterRequest request) {
        Pair<UserTokenResponse, CreationReport> result = registerUserUC.register(request);
        if (result.getSecond().hasErrors()) {
            return SuperResult.failure(Report.failure(result.getSecond().getErrors()));
        }
        var domainEvents = result.getSecond().getNotifications();
        publisher.publishAll(domainEvents);
        return SuperResult.success(result.getFirst());
    }

    @Transactional
    @Override
    public Result<UserTokenResponse> refresh(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Result.failure(AppError.UNPROCESSABLE_ENTITY
                    .withField("Authorization")
                    .withMessage("Authorization header is missing or invalid."));
        }
        var refreshToken = authHeader.substring(7);
        return refreshTokenUC.refresh(refreshToken);
    }
}