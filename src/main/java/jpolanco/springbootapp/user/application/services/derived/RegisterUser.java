package jpolanco.springbootapp.user.application.services.derived;

import jpolanco.springbootapp.shared.domain.CreationReport;
import jpolanco.springbootapp.shared.utils.Pair;
import jpolanco.springbootapp.user.application.ports.input.QRProvider;
import jpolanco.springbootapp.user.application.uc.base.CreateUserUC;
import jpolanco.springbootapp.user.application.uc.derived.RegisterUserUC;
import jpolanco.springbootapp.user.application.uc.unique.GenerateJwtUC;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.RegisterRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
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
