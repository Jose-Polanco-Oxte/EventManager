package jpolanco.springbootapp.event.domain.model.valueobjects;
import jpolanco.springbootapp.event.domain.errors.EventDomainError;
import jpolanco.springbootapp.shared.domain.Result;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Categories {
    private Set<String> Values;

    private Categories(Set<String> values) {
        this.Values = values;
    }

    public static Result<Categories> create(List<String> values) {
        if (values == null || values.isEmpty()) {
            return Result.failure(EventDomainError.NULL_VALUE.field("Categories"));
        }
        for (String value : values) {
            if (value == null || value.isEmpty()) {
                return Result.failure(EventDomainError.INVALID_CATEGORY);
            }
        }
        return Result.success(new Categories(new HashSet<>(values)));
    }

    public List<String> getValues() {
        return Values.stream().toList();
    }

    public void addCategory(String category) {
        if (category == null || category.isBlank()) return;
        this.Values.add(category);
    }

    public void addCategories(List<String> categories) {
        this.Values.addAll(categories);
    }

    public void removeCategory(String category) {
        this.Values.remove(category);
    }

    public boolean containsCategory(String category) {
        return this.Values.contains(category);
    }

    public boolean isEmpty() {
        return this.Values.isEmpty();
    }

    public int size() {
        return this.Values.size();
    }
}
