package jpolanco.applicationcore.user.domain.services;

import jpolanco.applicationcore.shared.domain.utils.abstracterrors.DomainError;
import jpolanco.applicationcore.shared.domain.utils.primitives.results.Result;
import jpolanco.applicationcore.user.domain.model.User;
import jpolanco.applicationcore.user.domain.model.UserStatus;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserUpdatePolicyService {
    private final DeactivationPolicyService deactivationPolicyService;
    private final ReactivationPolicyService reactivationPolicyService;
    private final SuspensionPolicyService suspensionPolicyService;

    public Result<Void, DomainError> checkChangeStatus(User user, User targetUser, UserStatus statusToChange) {
        if (statusToChange == null) {
            return Result.failure(DomainError.businessRule("Status to change cannot be null")
                    .withDetails("user: " + user.getUserId().getUUID() + ", target: " + targetUser.getUserId().getUUID()));
        }
        switch (statusToChange) {
            case ACTIVE -> {
                return reactivationPolicyService.check(user, targetUser);
            }
            case INACTIVE -> {
                return deactivationPolicyService.check(user, targetUser);
            }
            case SUSPENDED -> {
                return suspensionPolicyService.check(user, targetUser);
            }
            default -> {
                return Result.failure(DomainError.businessRule("Invalid status change requested")
                        .withDetails("user: " + user.getUserId().getUUID() + ", target: " + targetUser.getUserId().getUUID()));
            }
        }
    }

    public Result<Void, DomainError> checkAbleToUpdateSelf(User user) {
        if (user.isSuspended()) {
            return Result.failure(DomainError.businessRule("You cannot update your profile while suspended")
                    .withDetails("user: " + user.getUserId().getUUID()));
        }

        if (user.isInactive()) {
            return Result.failure(DomainError.businessRule("You cannot update your profile while inactive")
                    .withDetails("user: " + user.getUserId().getUUID()));
        }

        return Result.success();
    }

    public Result<Void, DomainError> checkAbleToUpdate(User maybeAdmin) {
        if (!maybeAdmin.isAdmin()) {
            return Result.failure(DomainError.businessRule("You must be an admin to update users")
                    .withDetails("user: " + maybeAdmin.getUserId().getUUID()));
        }

        if (maybeAdmin.isSuspended()) {
            return Result.failure(DomainError.businessRule("You cannot update users if you are suspended")
                    .withDetails("user: " + maybeAdmin.getUserId().getUUID()));
        }

        if (maybeAdmin.isInactive()) {
            return Result.failure(DomainError.businessRule("You cannot update users if you are inactive")
                    .withDetails("user: " + maybeAdmin.getUserId().getUUID()));
        }

        return Result.success();
    }
}
