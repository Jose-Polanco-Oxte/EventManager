package jpolanco.springbootapp.event.infrastructure.adapters.mappers.entity;

import jpolanco.springbootapp.event.domain.model.valueobjects.Categories;
import jpolanco.springbootapp.event.infrastructure.adapters.output.persistence.CategoryEntity;
import jpolanco.springbootapp.shared.infrastructure.EntityMapper;

import java.util.List;

public interface CategoryEntityMapper extends EntityMapper<List<CategoryEntity>, Categories> {
}
