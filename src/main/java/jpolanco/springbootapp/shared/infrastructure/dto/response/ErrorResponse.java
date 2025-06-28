package jpolanco.springbootapp.shared.infrastructure.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jpolanco.springbootapp.shared.infrastructure.dto.interfaces.Dto;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ErrorResponse(
        String field,
        String message,
        int code,
        String details
) implements Dto {}
