package jpolanco.springbootapp.event.infrastructure.adapters.output.repository;

import jpolanco.springbootapp.event.infrastructure.adapters.output.persistence.CategoryEntity;
import org.springframework.data.repository.CrudRepository;

public interface JpaCategoryRepository extends CrudRepository<CategoryEntity, String> {
}
