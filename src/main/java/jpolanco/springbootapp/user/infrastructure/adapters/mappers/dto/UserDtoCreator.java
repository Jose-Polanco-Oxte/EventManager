package jpolanco.springbootapp.user.infrastructure.adapters.mappers.dto;

import jpolanco.springbootapp.shared.application.DtoCreator;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserResponseDto;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.domain.model.valueobjects.Role;
import org.springframework.stereotype.Component;

@Component("UserDto")
public class UserDtoCreator implements DtoCreator<User, UserResponseDto> {
    @Override
    public UserResponseDto create(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getStatus(),
                user.getRoles().stream()
                        .map(Role::getValue)
                        .toList()
        );
    }
}
