package jpolanco.springbootapp.event.domain.model.valueobjects;
import jpolanco.springbootapp.shared.domain.Error;
import jpolanco.springbootapp.shared.domain.Result;

import java.util.List;

public class Categories {
    private List<String> Values;

    private Categories(List<String> values) {
        this.Values = values;
    }

    public static Result<Categories> create(List<String> values) {
        if (values == null || values.isEmpty()) {
            return Result.failure(Error.NULL_VALUE.field("Categories"));
        }
        for (String value : values) {
            if (value == null || value.isEmpty()) {
                return Result.failure(new Error("INVALID_CATEGORY", "Category cannot be empty"));
            }
        }
        return Result.success(new Categories(values));
    }

    public List<String> getValues() {
        return Values;
    }

    public void addCategory(String category) {
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
