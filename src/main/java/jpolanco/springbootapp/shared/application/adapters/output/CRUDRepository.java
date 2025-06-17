package jpolanco.springbootapp.shared.application.adapters.output;

import java.util.Optional;

public interface CRUDRepository <T, ID>{
    T save(T entity);
    Optional<T> findById(ID id);
    void deleteById(ID id);
}