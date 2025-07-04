package jpolanco.springbootapp.user.infrastructure.services.interfaces;

import jpolanco.springbootapp.shared.utils.results.reports.UpdateReport;
import jpolanco.springbootapp.shared.utils.results.Result;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.ChangeEmailRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.ChangeNameRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.ChangePasswordRequest;

import java.util.UUID;

public interface ProfileService {
    Result<Void> changeEmail(UUID userId, ChangeEmailRequest request);
    UpdateReport changeName(UUID userId, ChangeNameRequest request);
    Result<Void> delete(UUID userId, String reason);
    Result<Void> changePassword(UUID userId, ChangePasswordRequest dto);
    Result<Void> deactivate(UUID userId, String reason);
    Result<Void> reactivate(UUID userId);
}