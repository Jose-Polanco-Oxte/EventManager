package jpolanco.springbootapp.user.infrastructure.services.implementations;

import jpolanco.springbootapp.shared.application.AppError;
import jpolanco.springbootapp.shared.utils.results.reports.CreationReport;
import jpolanco.springbootapp.shared.utils.results.Report;
import jpolanco.springbootapp.shared.utils.results.Result;
import jpolanco.springbootapp.shared.utils.Pair;
import jpolanco.springbootapp.shared.utils.results.SuperResult;
import jpolanco.springbootapp.user.application.usecase.derived.RegisterAccount;
import jpolanco.springbootapp.user.application.usecase.unique.RefreshToken;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserTokenResponse;
import jpolanco.springbootapp.user.application.usecase.unique.Login;
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
    private final Login login;
    private final RegisterAccount registerAccount;
    private final RefreshToken refreshToken;
    private final DomainEventsPublisher publisher;

    @Transactional
    @Override
    public Result<UserTokenResponse> login(LoginRequest request) {
        return login.login(request);
    }

    @Transactional
    @Override
    public SuperResult<UserTokenResponse, Report> register(RegisterRequest request) {
        Pair<UserTokenResponse, CreationReport> result = registerAccount.register(request);
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
        return this.refreshToken.refresh(refreshToken);
    }
}