package jpolanco.springbootapp.user.domain.model.value_objects;

import jpolanco.springbootapp.shared.domain.utils.DomainError;
import jpolanco.springbootapp.shared.domain.IdObject;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.utils.Validators;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class UserId extends IdObject {

    private UserId(Long userId, UUID uuid) {
        super(uuid, userId);
    }

    public static Result<UserId> create(UUID uuid) {
        if (uuid == null) {
            return Result.failure(DomainError.NULL_VALUE
                    .withDetails("UserId and UUID cannot be null")
                    .withField("UserId"));
        }
        return Result.success(new UserId(null, uuid));
    }

    protected static UserId loadUnchecked(Long userId, UUID uuid) {
        return new UserId(userId, uuid);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UserId that)) return false;
        return Objects.equals(this.id, that.id) &&
               Objects.equals(this.uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid);
    }

    @Override
    public String toString() {
        return "UserId{" +
                "id=" + id +
                ", uuid=" + uuid +
                '}';
    }
}
