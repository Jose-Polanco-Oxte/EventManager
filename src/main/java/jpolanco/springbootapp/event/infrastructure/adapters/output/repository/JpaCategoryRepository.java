package jpolanco.springbootapp.event.infrastructure.adapters.output.repository;

import jpolanco.springbootapp.event.infrastructure.adapters.output.persistence.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCategoryRepository extends JpaRepository<CategoryEntity, String> {
}
