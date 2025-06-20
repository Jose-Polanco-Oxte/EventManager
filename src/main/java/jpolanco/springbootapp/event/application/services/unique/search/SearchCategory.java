package jpolanco.springbootapp.event.application.services.unique.search;

import jpolanco.springbootapp.event.application.ports.output.CategoriesRepository;
import jpolanco.springbootapp.event.application.uc.unique.search.SearchCategoryUC;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchCategory implements SearchCategoryUC {
    private final CategoriesRepository categoriesRepository;

    @Override
    public List<String> search(String name, int size) {
        return categoriesRepository.search(name, size);
    }
}
