package jpolanco.springbootapp.user.infrastructure.services.interfaces;

import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserResponseDto;

import java.util.List;

public interface SearchUserService {
    List<UserResponseDto> searchUsersByName(String name, int size);
    List<UserResponseDto> searchUsersByEmail(String email, int size);
}
