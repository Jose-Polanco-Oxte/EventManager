package jpolanco.applicationcore.user.domain.services;

import jpolanco.applicationcore.shared.domain.utils.abstracterrors.DomainError;
import jpolanco.applicationcore.shared.domain.utils.primitives.results.Result;
import jpolanco.applicationcore.user.domain.model.User;

public class DeletionPolicyService {

    public Result<Void, DomainError> checkSelf(User user) {
        if (user.isAdmin()) {
            return Result.failure(DomainError.businessRule("Admin users cannot delete themselves")
                    .withDetails("user: " + user.getUserId().getUUID()));
        }

        if (user.isSuspended()) {
            return Result.failure(DomainError.businessRule("Suspended users cannot be deleted")
                    .withDetails("user: " + user.getUserId().getUUID()));
        }

        return Result.success();
    }

    public Result<Void, DomainError> check(User user, User targetUser) {

        if (!user.isAdmin()) {
            return Result.failure(DomainError.businessRule("You must be an admin to delete users")
                    .withDetails("user: " + user.getUserId().getUUID()));
        }

        if (user.isSuspended()) {
            return Result.failure(DomainError.businessRule("You cannot delete users if you are suspended")
                    .withDetails("user: " + user.getUserId().getUUID()));
        }

        if (user.isInactive()) {
            return Result.failure(DomainError.businessRule("You cannot delete users if you are inactive")
                    .withDetails("user: " + user.getUserId().getUUID()));
        }

        if (targetUser.isSuspended()) {
            return Result.failure(DomainError.businessRule("You cannot delete a suspended user")
                    .withDetails("user: " + targetUser.getUserId().getUUID()));
        }

        if (targetUser.isAdmin() && !user.isAdmin()) {
            return Result.failure(DomainError.businessRule("You cannot delete an admin user")
                    .withDetails("user: " + targetUser.getUserId().getUUID()));
        }

        return Result.success();
    }
}
