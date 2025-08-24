package jpolanco.applicationcore.user.domain.services;

import jpolanco.applicationcore.shared.domain.utils.abstracterrors.DomainError;
import jpolanco.applicationcore.shared.domain.utils.primitives.results.Result;
import jpolanco.applicationcore.user.domain.model.User;

public class ReactivationPolicyService {

    public Result<Void, DomainError> checkSelf(User user) {
        if (user.isActive()) {
            return Result.failure(DomainError.businessRule("Your account is already active")
                    .withDetails("user: " + user.getUserId().getUUID()));
        }

        if (user.isAdmin()) {
            return Result.failure(DomainError.businessRule("Admin users cannot be reactivated themselves")
                    .withDetails("user: " + user.getUserId().getUUID()));
        }

        if (user.isSuspended()) {
            return Result.failure(DomainError.businessRule("Suspended users cannot reactivate their account")
                    .withDetails("user: " + user.getUserId().getUUID()));
        }

        return Result.success();
    }

    public Result<Void, DomainError> check(User user, User targetUser) {
        if (!user.isAdmin()) {
            return Result.failure(DomainError.businessRule("You cannot reactivate users")
                    .withDetails("user: " + user.getUserId().getUUID()));
        }

        if (user.isSuspended()) {
            return Result.failure(DomainError.businessRule("You cannot reactivate users if you are suspended")
                    .withDetails("user: " + user.getUserId().getUUID()));
        }

        if (user.isInactive()) {
            return Result.failure(DomainError.businessRule("You cannot reactivate users if you are inactive")
                    .withDetails("user: " + user.getUserId().getUUID()));
        }

        if (targetUser.isSuspended() && !user.isAdmin()) {
            return Result.failure(DomainError.businessRule("You cannot reactivate a suspended user")
                    .withDetails("user: " + targetUser.getUserId().getUUID()));
        }

        return Result.success();
    }
}
