package jpolanco.applicationcore.user.domain.dto;

import java.util.List;

public record RolesUpdate(
        List<String> add,
        List<String> remove
) {
}
