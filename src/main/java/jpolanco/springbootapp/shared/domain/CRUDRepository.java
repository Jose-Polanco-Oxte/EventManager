package jpolanco.springbootapp.shared.domain;

import java.util.Optional;

public interface CRUDRepository <T, ID>{
    T save(T entity);
    Optional<T> findById(ID id);
    void deleteById(ID id);
    T update(T entity);
}