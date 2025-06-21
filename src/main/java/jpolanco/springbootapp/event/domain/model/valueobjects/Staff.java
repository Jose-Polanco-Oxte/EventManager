package jpolanco.springbootapp.event.domain.model.valueobjects;

import jpolanco.springbootapp.event.domain.errors.EventDomainError;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.model.value_objects.UserId;
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
        Result<UserId> maybeUserId = UserId.create(userId);
        if (maybeUserId.isFailure()) {
            return Result.failure(maybeUserId.getError());
        }
        if (role == null || role.isBlank()) {
            return Result.failure(EventDomainError.INVALID_ROLE);
        }
        UserId userIdValue = maybeUserId.getValue();
        return Result.success(new Staff(userIdValue, role, assistanceClerk));
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Staff staff)) return false;
        return this.userId.equals(staff.userId);
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }
}
