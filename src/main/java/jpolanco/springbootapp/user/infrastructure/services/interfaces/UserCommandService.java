package jpolanco.springbootapp.user.infrastructure.services.interfaces;

import jpolanco.springbootapp.shared.domain.UpdateReport;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.AllFieldsUserUpdate;

import java.util.UUID;

public interface UserCommandService {
    UpdateReport update(UUID userId, AllFieldsUserUpdate request);
    Result<Void> deleteById(UUID userId, String reason);
    Result<Void> reactivateById(UUID userId);
    Result<Void> deactivateById(UUID userId, String reason);
    Result<Void> suspendById(UUID userId, String reason);
}