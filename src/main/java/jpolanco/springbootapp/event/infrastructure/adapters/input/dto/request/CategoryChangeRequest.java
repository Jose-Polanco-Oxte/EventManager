package jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request;

import java.util.List;

public record CategoryChangeRequest(
        List<String> add,
        List<String> remove
) {
}
