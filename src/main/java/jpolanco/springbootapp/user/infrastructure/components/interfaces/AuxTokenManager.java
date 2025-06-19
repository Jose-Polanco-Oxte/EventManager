package jpolanco.springbootapp.user.infrastructure.components.interfaces;

import jpolanco.springbootapp.user.domain.model.User;

public interface AuxTokenManager {
    void revokeAllUserTokens(String userId);

    void saveToken(User user, String jwtToken);

    boolean invalidTokenInDB(String token);

    void deleteExpiredTokensAndRevoke();

    boolean sessionLimitReached(String userId);

    void deleteAllUserTokens(String userId);
}