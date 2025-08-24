package jpolanco.applicationcore.user.domain.services;

import jpolanco.applicationcore.shared.domain.utils.abstracterrors.DomainError;
import jpolanco.applicationcore.shared.domain.utils.primitives.results.Result;
import jpolanco.applicationcore.user.domain.model.User;

public class SuspensionPolicyService {

    public Result<Void, DomainError> check(User user, User targetUser) {
        if (!user.isAdmin()) {
            return Result.failure(DomainError.businessRule("You cannot suspend users")
                    .withDetails("user: " + user.getUserId().getUUID()));
        }

        if (user.isSuspended()) {
            return Result.failure(DomainError.businessRule("You cannot suspend users if you are suspended")
                    .withDetails("user: " + user.getUserId().getUUID()));
        }

        if (user.isInactive()) {
            return Result.failure(DomainError.businessRule("You cannot suspend users if you are inactive")
                    .withDetails("user: " + user.getUserId().getUUID()));
        }

        if (targetUser.isSuspended()) {
            return Result.failure(DomainError.businessRule("The user is already suspended")
                    .withDetails("user: " + targetUser.getUserId().getUUID()));
        }

        if (targetUser.isAdmin() && !user.isAdmin()) {
            return Result.failure(DomainError.businessRule("You cannot suspend an admin user")
                    .withDetails("user: " + targetUser.getUserId().getUUID()));
        }
        return Result.success();
    }
}
