package jpolanco.springbootapp.user.application.default_services.derived;

import jpolanco.springbootapp.shared.domain.CreationReport;
import jpolanco.springbootapp.shared.utils.Pair;
import jpolanco.springbootapp.user.application.ports.input.QRProvider;
import jpolanco.springbootapp.user.application.use_case.base.CreateUserUC;
import jpolanco.springbootapp.user.application.use_case.derived.RegisterUserUC;
import jpolanco.springbootapp.user.application.use_case.unique.GenerateJwtUC;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.RegisterRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserTokenResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RegisterUser implements RegisterUserUC {
    private final CreateUserUC createUserUC;
    private final GenerateJwtUC generateJwtUC;
    private final QRProvider qrProvider;

    @Override
    public Pair<UserTokenResponse, CreationReport> register(RegisterRequest request) {
        var result = createUserUC.create(
                request.email(),
                request.firstName(),
                request.lastName(),
                request.password()
        );
        if (result.isFailure()) {
            return new Pair<>(null, CreationReport.failed(result.getFailure().getErrors()));
        }
        var user = result.getSuccess();
        var maybeTokens = generateJwtUC.create(user);
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
