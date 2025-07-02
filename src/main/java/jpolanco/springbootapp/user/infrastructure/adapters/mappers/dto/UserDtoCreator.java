package jpolanco.springbootapp.user.infrastructure.adapters.mappers.dto;

import jpolanco.springbootapp.shared.infrastructure.dto.interfaces.DtoCreator;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserResponse;
import jpolanco.springbootapp.user.domain.model.value_objects.User;
import org.springframework.stereotype.Component;

@Component()
public class UserDtoCreator implements DtoCreator<User, UserResponse> {
    @Override
    public UserResponse create(User user) {
        return new UserResponse(
                user.getUUID(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getStatus().getValue(),
                user.getRoles().stream().toList()
        );
    }

    public static UserDtoCreator getInstance() {
        return new UserDtoCreator();
    }
}
