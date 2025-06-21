package jpolanco.springbootapp.user.application.ports.output;

public interface RolesRepository {
    boolean existsByName(String name);
    void save(String name);
}
