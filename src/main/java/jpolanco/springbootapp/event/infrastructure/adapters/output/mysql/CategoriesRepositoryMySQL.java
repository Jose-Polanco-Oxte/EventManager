package jpolanco.springbootapp.event.infrastructure.adapters.output.mysql;

import jpolanco.springbootapp.event.application.ports.output.CategoriesRepository;
import jpolanco.springbootapp.event.infrastructure.adapters.output.persistence.CategoryEntity;
import jpolanco.springbootapp.event.infrastructure.adapters.output.repository.JpaCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class CategoriesRepositoryMySQL implements CategoriesRepository {
    private final JpaCategoryRepository jpaCategoryRepository;
    @Override
    public List<String> search(String name, int size) {
        return jpaCategoryRepository.searchByName(name, PageRequest.of(0, size))
                .map(CategoryEntity::getName)
                .toList();
    }

    @Override
    public String save(String entity) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(entity);
        jpaCategoryRepository.save(categoryEntity);
        return categoryEntity.getName();
    }

    @Override
    public Optional<String> findById(String s) {
        return jpaCategoryRepository.findById(s)
                .map(CategoryEntity::getName);
    }

    @Override
    public void deleteById(String s) {
        jpaCategoryRepository.deleteById(s);
    }
}
