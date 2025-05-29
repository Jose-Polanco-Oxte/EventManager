package jpolanco.springbootapp.user.infrastructure.adapters.mappers.dto;

import jpolanco.springbootapp.shared.application.DtoCreator;
import jpolanco.springbootapp.shared.application.Dto;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserDataResponseDto;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.domain.model.valueobjects.Role;
import org.springframework.stereotype.Component;

@Component("UserDto")
public class UserDtoCreator implements DtoCreator<User> {
    @Override
    public Dto create(User user) {
        return new UserDataResponseDto(
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
