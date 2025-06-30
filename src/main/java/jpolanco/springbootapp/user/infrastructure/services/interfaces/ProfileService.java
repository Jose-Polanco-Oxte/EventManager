package jpolanco.springbootapp.user.infrastructure.services.interfaces;

import jpolanco.springbootapp.shared.domain.UpdateReport;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.ChangeEmailRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.ChangeNameRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.ChangePasswordRequest;

public interface ProfileService {
    Result<Void> changeEmail(String userId, ChangeEmailRequest request);
    UpdateReport changeName(String userId, ChangeNameRequest request);
    Result<Void> delete(String userId, String reason);
    Result<Void> changePassword(String userId, ChangePasswordRequest dto);
    Result<Void> deactivate(String userId, String reason);
    Result<Void> reactivate(String userId);
}