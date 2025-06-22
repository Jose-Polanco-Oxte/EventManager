package jpolanco.springbootapp.user.infrastructure.services.interfaces;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.UpdateEmailRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.UpdateNameRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.UpdatePasswordRequest;

public interface ProfileService {
    Result<Void> changeEmail(String userId, UpdateEmailRequest request);
    Result<Void> changeName(String userId, UpdateNameRequest request);
    Result<Void> delete(String userId, String reason);
    Result<Void> changePassword(String userId, UpdatePasswordRequest dto);
    Result<Void> deactivate(String userId, String reason);
    Result<Void> reactivate(String userId);
}