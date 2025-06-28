package jpolanco.springbootapp.user.infrastructure.services.interfaces;

import jpolanco.springbootapp.shared.domain.Report;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.AllFieldsUserUpdate;

public interface UserCommandService {
    Report update(AllFieldsUserUpdate request, String userId);
    Result<Void> deleteById(String userId, String reason);
    Result<Void> reactivateById(String userId);
    Result<Void> deactivateById(String userId, String reason);
    Result<Void> suspendById(String userId, String reason);
}