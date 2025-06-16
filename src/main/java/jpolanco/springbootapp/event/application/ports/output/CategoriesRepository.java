package jpolanco.springbootapp.event.application.ports.output;

import jpolanco.springbootapp.shared.domain.CRUDRepository;

import java.util.List;

public interface CategoriesRepository extends CRUDRepository<String, String> {
    List<String> search(String name, int size);
}