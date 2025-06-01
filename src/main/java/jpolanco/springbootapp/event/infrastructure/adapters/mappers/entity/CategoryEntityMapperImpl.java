package jpolanco.springbootapp.event.infrastructure.adapters.mappers.entity;

import jpolanco.springbootapp.event.application.ports.output.CategoriesRepository;
import jpolanco.springbootapp.event.domain.model.valueobjects.Categories;
import jpolanco.springbootapp.event.infrastructure.adapters.output.persistence.CategoryEntity;
import jpolanco.springbootapp.event.infrastructure.adapters.output.repository.JpaCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class CategoryEntityMapperImpl implements CategoryEntityMapper {

    private final JpaCategoryRepository jpaCategoryRepository;

    @Override
    public List<CategoryEntity> toEntity(Categories domain) {
        var domainCategories = domain.getValues();
        var categoriesEntities = new ArrayList<CategoryEntity>();
        for (var category : domainCategories) {
            var entity = jpaCategoryRepository.findById(category);
            entity.ifPresent(categoriesEntities::add);
        }
        if (categoriesEntities.isEmpty()) {
            throw new IllegalArgumentException("No categories found for the provided domain values.");
        }
        return categoriesEntities;
    }

    @Override
    public Categories toDomain(List<CategoryEntity> entity) {
        var categories = new ArrayList<String>();
        for (var categoryEntity : entity) {
            categories.add(categoryEntity.getName());
        }
        var maybeCategories = Categories.create(categories);
        if (maybeCategories.isFailure()) {
            throw new IllegalArgumentException("Invalid categories provided.");
        }
        return maybeCategories.getValue();
    }
}
