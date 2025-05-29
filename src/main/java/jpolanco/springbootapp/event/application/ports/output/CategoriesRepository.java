package jpolanco.springbootapp.event.application.ports.output;

import jpolanco.springbootapp.event.domain.model.valueobjects.Categories;
import jpolanco.springbootapp.shared.domain.CRUDRepository;

public interface CategoriesRepository extends CRUDRepository<Categories, Integer> {
}