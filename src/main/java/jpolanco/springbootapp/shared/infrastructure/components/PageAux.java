package jpolanco.springbootapp.shared.infrastructure.components;

import org.springframework.data.domain.Sort;

import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

public class PageAux {
    public static Sort resolveSort(String sortBy, String sortOrder) {
        if (sortBy.equalsIgnoreCase("none")) {
            return Sort.unsorted();
        }
        return sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(Sort.Direction.ASC, sortBy) :
                Sort.by(Sort.Direction.DESC, sortBy);
    }
}
