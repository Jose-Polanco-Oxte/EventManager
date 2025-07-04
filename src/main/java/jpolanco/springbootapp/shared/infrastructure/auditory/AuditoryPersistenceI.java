package jpolanco.springbootapp.shared.infrastructure.mappers;

public interface AuditoryPersistenceI {
    void save(Object event);
}
