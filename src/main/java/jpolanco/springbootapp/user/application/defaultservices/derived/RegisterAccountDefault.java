package jpolanco.springbootapp.user.application.defaultservices.derived;

import jpolanco.springbootapp.shared.utils.results.reports.CreationReport;
import jpolanco.springbootapp.shared.utils.Pair;
import jpolanco.springbootapp.user.application.ports.input.QRProvider;
import jpolanco.springbootapp.user.application.usecase.base.CreateUser;
import jpolanco.springbootapp.user.application.usecase.derived.RegisterAccount;
import jpolanco.springbootapp.user.application.usecase.unique.GenerateToken;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.RegisterRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserTokenResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RegisterAccountDefault implements RegisterAccount {
    private final CreateUser createUser;
    private final GenerateToken generateToken;
    private final QRProvider qrProvider;

    @Override
    public Pair<UserTokenResponse, CreationReport> register(RegisterRequest request) {
        var result = createUser.create(
                request.email(),
                request.firstName(),
                request.lastName(),
                request.password()
        );
        if (result.isFailure()) {
            return new Pair<>(null, CreationReport.failed(result.getFailure().getErrors()));
        }
        var user = result.getSuccess();
        var maybeTokens = generateToken.create(user);
        if (maybeTokens.isFailure()) {
            return new Pair<>(null, CreationReport.failed(maybeTokens.getError()));
        }
        var qr = qrProvider.generate(user.getEmail());
        qrProvider.save(qr, user.getQRFileName());
        return new Pair<>(
                maybeTokens.getValue(),
                CreationReport.created(user.pullEvents())
        );
    }
}
