package jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request;

import java.util.List;

public record UserRoleChangeRequest(
        List<String> add,
        List<String> remove
) {
}
