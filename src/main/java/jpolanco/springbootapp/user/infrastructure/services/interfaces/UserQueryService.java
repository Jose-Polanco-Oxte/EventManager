package jpolanco.springbootapp.user.infrastructure.services.interfaces;

import jpolanco.springbootapp.shared.infrastructure.dto.CursorPageResponseDto;
import jpolanco.springbootapp.shared.infrastructure.dto.PageResponseDto;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserResponse;

import java.util.Optional;

public interface UserQueryService {
    Optional<UserResponse> getById(String userId);
    Optional<UserResponse> getByEmail(String email);
    PageResponseDto<UserResponse> get(int page, int size, String sortBy, String orderBy);
    CursorPageResponseDto<UserResponse, String> get(String cursor, int size, String sortBy, String orderBy);
}