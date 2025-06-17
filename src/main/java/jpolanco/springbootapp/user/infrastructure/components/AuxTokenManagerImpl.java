package jpolanco.springbootapp.user.infrastructure.components;

import jpolanco.springbootapp.user.application.errors.IllegalUserOperation;
import jpolanco.springbootapp.user.application.ports.input.AuxTokenManager;
import jpolanco.springbootapp.user.application.ports.output.JwtRepository;
import jpolanco.springbootapp.user.application.ports.output.UserRepository;
import jpolanco.springbootapp.user.domain.model.Token;
import jpolanco.springbootapp.shared.domain.TokenStatus;
import jpolanco.springbootapp.shared.domain.TokenType;
import jpolanco.springbootapp.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuxTokenManagerImpl implements AuxTokenManager {

    private final JwtRepository jwtRepository;
    private final UserRepository userRepository;

    @Value("${jwt.expiration}")
    private long expiration;

    public void revokeAllUserTokens(String userId) {
        var validUserTokens = jwtRepository.findAllByUserId(userId);
        for (Token token : validUserTokens) {
            if (token.getStatus() == TokenStatus.ACTIVE) {
                token.changeStatus(TokenStatus.REVOKED);
            }
        }
        if (!validUserTokens.isEmpty()) {
            for (Token token : validUserTokens) {
                token.changeStatus(TokenStatus.REVOKED);
            }
            jwtRepository.saveAll(validUserTokens);
        }
    }

    public void deleteAllUserTokens(String userId) {
        jwtRepository.deleteAllByUserId(userId);
    }

    public void saveToken(User user, String jwtToken) {
        var maybeToken = Token.create (
                user.getId(),
                jwtToken,
                TokenType.BEARER,
                expiration
        );
        if (maybeToken.isFailure()) {
            throw new IllegalUserOperation(maybeToken.getMessage());
        }
        var token = maybeToken.getValue();
        jwtRepository.save(token);
    }

    public boolean invalidTokenInDB(String token) {
        var maybeToken = jwtRepository.findByToken(token);
        if (maybeToken.isEmpty()) {
            return false;
        }
        var dbToken = maybeToken.get();
        return dbToken.isRevoked() || dbToken.isExpired();
    }

    @Override
    public void deleteExpiredTokensAndRevoke() {

    }

    public boolean sessionLimitReached(String email) {
        var user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return false;
        }
        var userId = user.get().getId();
        var sessions = jwtRepository.countSessionsByUserId(userId);
        return sessions >= 5; // Assuming a limit of 5 active sessions
    }
}
