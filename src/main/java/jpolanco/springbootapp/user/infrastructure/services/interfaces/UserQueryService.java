package jpolanco.springbootapp.user.infrastructure.services.interfaces;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.infrastructure.dto.CursorPageResponseDto;
import jpolanco.springbootapp.shared.infrastructure.dto.PageResponseDto;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserResponseDto;

import java.util.List;

public interface UserQueryService {
    Result<UserResponseDto> getUserById(String userId);
    Result<UserResponseDto> getUserByEmail(String email);
    PageResponseDto<UserResponseDto> getUsers(int page, int size, String sortBy, String sortOrder);
    CursorPageResponseDto<UserResponseDto, String> getUsers(String cursor, int size, String sortBy, String sortOrder);
    List<UserResponseDto> getAll();
}
