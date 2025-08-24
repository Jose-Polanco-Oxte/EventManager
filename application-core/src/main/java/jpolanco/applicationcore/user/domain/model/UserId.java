package jpolanco.applicationcore.user.domain.model;

import jpolanco.applicationcore.shared.domain.utils.Validators;
import jpolanco.applicationcore.shared.domain.utils.abstracterrors.DomainError;
import jpolanco.applicationcore.shared.domain.utils.primitives.results.Result;
import jpolanco.applicationcore.shared.domain.vobjects.IdObject;

import java.util.Objects;
import java.util.UUID;

public class UserId extends IdObject {

    private UserId(Long userId, UUID uuid) {
        super(uuid, userId);
    }

    public static Result<UserId, DomainError> create(UUID uuid) {
        if (uuid == null) {
            return Validators.EMPTY_VALUE("UUID");
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
