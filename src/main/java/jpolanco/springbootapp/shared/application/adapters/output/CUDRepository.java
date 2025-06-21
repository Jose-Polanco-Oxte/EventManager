package jpolanco.springbootapp.shared.application.adapters.output;

public interface CUDRepository<T, ID> {
    T save(T entity);
    void deleteById(ID id);
}
