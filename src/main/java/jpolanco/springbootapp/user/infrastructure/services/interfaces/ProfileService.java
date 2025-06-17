package jpolanco.springbootapp.user.infrastructure.services.interfaces;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.infrastructure.SimpleResponseDto;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.UpdateEmailRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.UpdateNameRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.UpdatePasswordRequest;

public interface ProfileService {
    Result<SimpleResponseDto> changeName(String userId, UpdateNameRequest request);
    Result<SimpleResponseDto> changeEmail(String userId, UpdateEmailRequest request);
    Result<SimpleResponseDto> deleteMe(String userId);
    Result<SimpleResponseDto> changePassword(String userId, UpdatePasswordRequest dto);
}
