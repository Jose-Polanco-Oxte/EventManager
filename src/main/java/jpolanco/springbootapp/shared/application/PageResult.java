package jpolanco.springbootapp.shared.application;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<E> {
    private List<E> items;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean hasNext;

    public PageResult(List<E> items, int page, int size, boolean hasNext) {
        this.items = items;
        this.page = page;
        this.size = size;
        this.hasNext = hasNext;
    }
}