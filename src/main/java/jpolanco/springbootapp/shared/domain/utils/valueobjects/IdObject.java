package jpolanco.springbootapp.shared.domain.utils.valueobjects;

import java.util.UUID;

public abstract class IdObject {
    protected final UUID uuid;
    protected final Long id;

    public IdObject(UUID uuid, Long id) {
        this.uuid = uuid;
        this.id = id;
    }
    public UUID getUUID() {
        return uuid;
    }
    public Long getId() {
        return id;
    }
}
