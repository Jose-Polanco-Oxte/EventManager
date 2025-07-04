package jpolanco.springbootapp.user.application.usecase.derived;

import jpolanco.springbootapp.shared.utils.results.reports.CreationReport;
import jpolanco.springbootapp.shared.utils.Pair;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.RegisterRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserTokenResponse;

public interface RegisterAccount {
    /**
     * Registers a new user with the provided details.
     *
     * @param request the registration request containing user details
     * @return a Pair containing UserTokenResponse and CreationReport
     */
    Pair<UserTokenResponse, CreationReport> register(RegisterRequest request);
}
