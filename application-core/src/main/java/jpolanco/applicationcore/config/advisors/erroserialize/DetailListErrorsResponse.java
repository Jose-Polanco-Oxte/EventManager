package jpolanco.applicationcore.config.advisors.erroserialize;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.Instant;
import java.util.List;

public record DetailListErrorsResponse(
        @JsonSerialize(using = SingleElementListSerializer.class)
        @JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.NON_EMPTY)
        List<DetailErrorResponse> errors,
        Instant timestamp
) {
}
