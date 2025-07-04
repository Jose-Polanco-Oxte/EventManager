package jpolanco.springbootapp.user.application.defaultservices.base;

import jpolanco.springbootapp.shared.application.AppError;
import jpolanco.springbootapp.shared.utils.results.reports.UpdateReport;
import jpolanco.springbootapp.user.application.ports.input.EncoderProvider;
import jpolanco.springbootapp.user.application.ports.input.QRProvider;
import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.domain.model.UserUpdater;
import jpolanco.springbootapp.user.domain.model.valueobjects.User;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.AllFieldsUserUpdate;
import jpolanco.springbootapp.user.application.usecase.base.UpdateUser;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class UpdateUserDefault implements UpdateUser {
    private final UserQueryRepository queryRepository;
    private final UserCommandRepository commandRepository;
    private final EncoderProvider passwordEncoder;
    private final QRProvider qrProvider;

    @Override
    public UpdateReport setChanges(User user, AllFieldsUserUpdate request) {
        if (queryRepository.findByEmail(request.email()).isPresent()
                && !user.getEmail().equals(request.email())) {
            return UpdateReport.failure(AppError.CONFLICT
                    .withField("Email")
                    .concatMessage("with " + request.email() + " is already in use"));
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        String oldQRFileName = user.getQRFileName();
        var report = UserUpdater.updater(user)
                .firstName(request.firstName())
                .lastName(request.lastName())
                .roles(request.roles().add(), request.roles().remove())
                .status(request.status())
                .email(request.email())
                .password(encodedPassword)
                .generateNewQR()
                .update();
        if (report.isFailure()) return report;

        var userSaved = commandRepository.save(user);
        qrProvider.delete(oldQRFileName);
        var qr = qrProvider.generate(userSaved.getEmail());
        qrProvider.save(qr, userSaved.getQRFileName());
        return report;
    }

    @Override
    public UpdateReport setChanges(UUID userId, AllFieldsUserUpdate request) {
        var OptionalUser = queryRepository.findByUuid(userId);
        if (OptionalUser.isEmpty()) return UpdateReport.failure(AppError.idNotFound(userId, "User"));

        var user = OptionalUser.get();
        return setChanges(user, request);
    }
}