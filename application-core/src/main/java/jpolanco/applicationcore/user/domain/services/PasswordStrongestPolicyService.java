package jpolanco.applicationcore.user.domain.services;

import jpolanco.applicationcore.shared.domain.utils.Validators;
import jpolanco.applicationcore.shared.domain.utils.abstracterrors.DomainError;
import jpolanco.applicationcore.shared.domain.utils.primitives.results.Result;

/**
 * Service to enforce the strongest password policy.
 * The password must be between 5 and 64 characters long.
 * It should ideally contain at least one uppercase letter, one lowercase letter, one digit, and one special character.
 * <p><strong>TODO: Implement regex check for the strongest policy.
 */
public class PasswordStrongestPolicyService {
    private static final int MIN_LENGTH = 5;
    private static final int MAX_LENGTH = 64;
    private static final String ERROR_MESSAGE = "Password must be between " + MIN_LENGTH + " and " + MAX_LENGTH + ".";
    private static final String ERROR_MESSAGE_REGEX =
            "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character.";


    public Result<Void, DomainError> check(String password) {
        if (password == null || password.isEmpty()) {
            return Validators.EMPTY_VALUE("Password");
        }
        if (password.length() < MIN_LENGTH || password.length() > MAX_LENGTH) {
            return Result.failure(DomainError.businessRule("Password length must be between " + MIN_LENGTH + " and " + MAX_LENGTH + " characters").withDetails(ERROR_MESSAGE));
        }
        return Result.success();
    }
}
