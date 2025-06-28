package jpolanco.springbootapp.user.infrastructure.services.implementations;

import jpolanco.springbootapp.shared.domain.utils.Error;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.infrastructure.errors.InfrastructureError;
import jpolanco.springbootapp.shared.utils.Either;
import jpolanco.springbootapp.user.domain.model.User;
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

import java.util.List;
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
    public Result<Map<String, String>> login(LoginRequest request) {
        var verifiedUser = loginUC.login(request.email(), request.password());
        if (verifiedUser.isFailure()) {
            return Result.failure(verifiedUser.getError());
        }
        var user = verifiedUser.getValue();
        return jwtService.authenticate(user, request.password());
    }

    @Transactional
    @Override
    public Either<Map<String, String>, List<Error>> register(RegisterRequest request) {
        Either<User, List<Error>> either = createUserUc.create(request);
        if (either.isRight()) {
            return Either.right(either.getRight());
        }
        var user = either.getLeft();
        var tokens = jwtService.createTokens(user);
        if (tokens.isFailure()) {
            return Either.right(List.of(tokens.getError()));
        }
        publisher.publishAll(user.pullEvents());
        return Either.left(tokens.getValue());
    }

    @Transactional
    @Override
    public Result<Map<String, String>> refresh(String authHeader) {
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