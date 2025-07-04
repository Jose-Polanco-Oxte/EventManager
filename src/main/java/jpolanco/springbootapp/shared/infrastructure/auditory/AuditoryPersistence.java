package jpolanco.springbootapp.shared.infrastructure.auditory;

public interface AuditoryPersistence {
    void save(Object event);
}
