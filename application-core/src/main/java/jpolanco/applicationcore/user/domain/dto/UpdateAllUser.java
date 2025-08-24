package jpolanco.applicationcore.user.domain.dto;


public record UpdateAllUser(
        String firstName,
        String lastName,
        String email,
        String password,
        String status,
        RolesUpdate roles
) {
}
