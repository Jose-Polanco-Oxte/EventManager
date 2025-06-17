package jpolanco.springbootapp.user.infrastructure.services.interfaces;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.infrastructure.SimpleResponseDto;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.AnyUserUpdateRequest;

public interface UserCommandService {
    Result<SimpleResponseDto> updateUser(AnyUserUpdateRequest request, String userId);
    Result<SimpleResponseDto> deleteUserById(String userId);
}
