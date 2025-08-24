package jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import filters.CollectionFilter;
import filters.Filter;
import filters.operations.ComparableOperation;
import filters.operations.InOperation;
import filters.operations.TextCollectionOperation;
import filters.operations.TextOperation;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;

@Validated
@JsonInclude(JsonInclude.Include.NON_NULL)
public record DefaultUserFilterRequest(@Valid Filter<Instant, ComparableOperation> creation,
                                       @Valid Filter<String, TextOperation> email,
                                       @Valid Filter<String, TextOperation> name,
                                       @Valid CollectionFilter<Instant, InOperation> creationList,
                                       @Valid CollectionFilter<String, TextCollectionOperation> emailList,
                                       @Valid CollectionFilter<String, TextCollectionOperation> nameList,
                                       @Valid CollectionFilter<String, TextCollectionOperation> roleList) {
}