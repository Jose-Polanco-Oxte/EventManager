package jpolanco.springbootapp.user.infrastructure.adapters.mappers.dto;

import jpolanco.springbootapp.shared.infrastructure.dto.DtoCreator;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserResponse;
import jpolanco.springbootapp.user.domain.model.User;
import org.springframework.stereotype.Component;

@Component("UserDto")
public class UserDtoCreator implements DtoCreator<User, UserResponse> {
    @Override
    public UserResponse create(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getStatus(),
                user.getRoles().stream().toList()
        );
    }
}
