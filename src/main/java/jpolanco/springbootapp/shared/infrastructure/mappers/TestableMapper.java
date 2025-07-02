package jpolanco.springbootapp.shared.infrastructure.mappers;

public interface TestableMapper<E, D> {
    E toEntity(D domain);
    D toDomain(E entity);
}