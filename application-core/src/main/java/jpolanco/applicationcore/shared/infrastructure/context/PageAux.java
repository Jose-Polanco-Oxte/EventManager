package jpolanco.applicationcore.shared.infrastructure.context;

import jpolanco.applicationcore.shared.infrastructure.utils.PageableRequest;
import jpolanco.applicationcore.shared.infrastructure.utils.types.Direction;
import jpolanco.applicationcore.shared.infrastructure.utils.types.SortValue;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageAux {

    /**
     * Resolve the Sort object based on the provided SortValue and Direction.
     *
     * @param sortBy        the field to sort by
     * @param sortDirection the direction of the sort (ASC or DESC)
     * @return a Sort object representing the sorting criteria
     */
    private static Sort resolveSort(SortValue sortBy, Direction sortDirection) {
        if (sortBy == null) {
            return Sort.unsorted(); // return unsorted if sortBy is null
        }

        if (sortDirection == null) {
            return Sort.by(Sort.Direction.ASC, sortBy.getSortValue()); // default to ascending if direction is null
        }

        return Sort.by(Sort.Direction.fromString(sortDirection.getValue()), sortBy.getSortValue());
    }

    /**
     * Convert a PageableRequest object to a Pageable object.
     *
     * @param pageableRequest the PageableRequest containing pagination and sorting information
     * @return a Pageable object representing the pagination and sorting criteria
     * @throws IllegalArgumentException if pageableRequest or its sortBy is null
     */
    public static Pageable resolvePageRequest(PageableRequest pageableRequest) {
        if (pageableRequest == null || pageableRequest.getSortBy() == null) {
            throw new IllegalArgumentException("PageableQuery cannot be null");
        }
        Sort sort;
        if (pageableRequest.getSortBy().unsorted()) {
            sort = Sort.unsorted(); // No sorting applied
        } else {
            Direction sortDirection = pageableRequest.getDirection(); // Can be null
            sort = PageAux.resolveSort(pageableRequest.getSortBy(), sortDirection); // Resolve sort with possible null direction
        }
        int page = pageableRequest.getPage(); // Assuming page is zero-based
        int size = pageableRequest.getSize(); // Number of items per page
        return PageRequest.of(page, size, sort);
    }
}
