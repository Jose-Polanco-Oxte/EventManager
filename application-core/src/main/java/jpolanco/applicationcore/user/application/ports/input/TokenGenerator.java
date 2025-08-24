package jpolanco.applicationcore.user.application.ports.input;

import jpolanco.applicationcore.shared.domain.utils.abstracterrors.ServiceError;
import jpolanco.applicationcore.shared.domain.utils.primitives.results.Result;
import jpolanco.applicationcore.user.domain.model.User;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.response.UserTokenResponse;

/**
 * Interface for generating and extracting information from tokens, such as JWTs.
 * Implementations should provide methods to generate tokens for users
 * and extract usernames from tokens.
 */
public interface TokenGenerator {
    Result<UserTokenResponse, ServiceError> generateToken(User user);

    Result<String, ServiceError> extractUsername(String token);
}