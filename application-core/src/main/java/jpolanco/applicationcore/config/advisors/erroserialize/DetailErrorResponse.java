package jpolanco.applicationcore.config.advisors.erroserialize;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.NON_EMPTY)
public record DetailErrorResponse(String field, String message) {
}
