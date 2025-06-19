package jpolanco.springbootapp.shared.infrastructure.mappers;

public interface EntityMapper<E, D> {
    E toEntity(D domain);
    D toDomain(E entity);
}