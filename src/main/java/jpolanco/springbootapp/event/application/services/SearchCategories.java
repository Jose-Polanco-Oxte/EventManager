package jpolanco.springbootapp.event.application.services;

import jpolanco.springbootapp.event.application.ports.output.CategoriesRepository;
import jpolanco.springbootapp.event.application.uc.SearchCategoriesUC;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchCategories implements SearchCategoriesUC {
    private final CategoriesRepository categoriesRepository;
    @Override
    public List<String> search(String name, int size) {
        return categoriesRepository.search(name, size);
    }
}
