package jpolanco.applicationcore.shared.domain.vobjects;

import java.util.UUID;

/**
 * Abstract base class for objects
 * that have both a UUID and a Long ID.
 * Provides getters for both identifiers.
 */
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
