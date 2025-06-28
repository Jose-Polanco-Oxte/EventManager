package jpolanco.springbootapp.shared.infrastructure.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import jpolanco.springbootapp.shared.infrastructure.dto.interfaces.Dto;

import java.util.List;

public record CursorPageResponse<T, ID>(
        @JsonAlias("data")
        List<T> content,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonAlias("lastCursor")
        ID lastItemId,

        boolean hasNextPage
) implements Dto {}
