package jpolanco.springbootapp.event.domain.model.valueobjects;

import jpolanco.springbootapp.shared.domain.Error;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.model.valueobjects.UserId;
import lombok.Getter;

@Getter
public class Staff {
    private final UserId userId;
    private final String role;
    private final boolean assistanceClerk;


    private Staff(UserId userId, String role, boolean assistanceClerk) {
        this.userId = userId;
        this.role = role;
        this.assistanceClerk = assistanceClerk;
    }

    public static Result<Staff> create(String userId, String role, boolean assistanceClerk) {
        var maybeUserId = UserId.create(userId);
        if (maybeUserId.isFailure()) {
            return Result.failure(maybeUserId.getError());
        }
        if (userId == null) {
            return Result.failure(new Error("USER_ID_IS_NULL", "userId cannot be null"));
        }
        if (role == null || role.isBlank()) {
            return Result.failure(new Error("INVALID_ROLE", "role cannot be empty"));
        }
        var userIdValue = maybeUserId.getValue();
        return Result.success(new Staff(userIdValue, role, assistanceClerk));
    }
}
