package jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.josepolanco.filterable.filters.CollectionFilter;
import io.github.josepolanco.filterable.filters.Filter;
import io.github.josepolanco.filterable.filters.operations.ComparableOperation;
import io.github.josepolanco.filterable.filters.operations.InOperation;
import io.github.josepolanco.filterable.filters.operations.TextCollectionOperation;
import io.github.josepolanco.filterable.filters.operations.TextOperation;
import jakarta.validation.Valid;
import jpolanco.domainmodel.user.UserStatusE;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.util.Optional;

@Validated
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AdminUserFilterRequest(@Valid Filter<Instant, ComparableOperation> creation,
                                     @Valid Filter<String, TextOperation> name,
                                     @Valid Filter<String, TextOperation> email,
                                     @Valid Filter<UserStatusE, ComparableOperation> status,
                                     @Valid Optional<Boolean> deleted, @Valid Filter<String, TextOperation> role,
                                     @Valid CollectionFilter<Instant, InOperation> creationList,
                                     @Valid CollectionFilter<String, TextCollectionOperation> nameList,
                                     @Valid CollectionFilter<String, TextCollectionOperation> emailList,
                                     @Valid CollectionFilter<UserStatusE, InOperation> statusList,
                                     @Valid CollectionFilter<String, TextCollectionOperation> roleList) {
}