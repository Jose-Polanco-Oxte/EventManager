package jpolanco.applicationcore.user.application.mappers;

import jpolanco.applicationcore.user.domain.model.User;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.response.UserResponse;

public class UserDto {

    public static UserResponse toResponse(User userResponse) {
        return new UserResponse(
                userResponse.getUserId().getUUID(),
                userResponse.getName().getFirstName(),
                userResponse.getName().getLastName(),
                userResponse.getEmail().getValue(),
                userResponse.getStatus().name(),
                userResponse.getRoles().get()
        );
    }
}
