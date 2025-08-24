package jpolanco.applicationcore.user.domain.services;

import jpolanco.applicationcore.shared.domain.utils.abstracterrors.DomainError;
import jpolanco.applicationcore.shared.domain.utils.primitives.results.Result;
import jpolanco.applicationcore.user.domain.model.User;

public class DeactivationPolicyService {

    public Result<Void, DomainError> checkSelf(User user) {
        if (user.isInactive()) {
            return Result.failure(DomainError.businessRule("Your account is already deactivated")
                    .withDetails("user: " + user.getUserId().getUUID()));
        }

        if (user.isAdmin()) {
            return Result.failure(DomainError.businessRule("Admin users cannot be deactivated themselves")
                    .withDetails("user: " + user.getUserId().getUUID()));
        }

        if (user.isSuspended()) {
            return Result.failure(DomainError.businessRule("Suspended users cannot deactivate their account")
                    .withDetails("user: " + user.getUserId().getUUID()));
        }

        return Result.success();
    }

    public Result<Void, DomainError> check(User user, User targetUser) {

        if (user.isSuspended()) {
            return Result.failure(DomainError.businessRule("You cannot deactivate users if you are suspended")
                    .withDetails("user: " + user.getUserId().getUUID()));
        }

        if (user.isInactive()) {
            return Result.failure(DomainError.businessRule("You cannot deactivate users if you are inactive")
                    .withDetails("user: " + user.getUserId().getUUID()));
        }

        if (targetUser.isSuspended()) {
            return Result.failure(DomainError.businessRule("You cannot deactivate a suspended user")
                    .withDetails("user: " + targetUser.getUserId().getUUID()));
        }

        if (targetUser.isInactive()) {
            return Result.failure(DomainError.businessRule("You cannot deactivate an inactive user")
                    .withDetails("user: " + targetUser.getUserId().getUUID()));
        }

        if (targetUser.isAdmin() && !user.isAdmin()) {
            return Result.failure(DomainError.businessRule("You cannot deactivate an admin user")
                    .withDetails("user: " + targetUser.getUserId().getUUID()));
        }

        return Result.success();
    }
}
