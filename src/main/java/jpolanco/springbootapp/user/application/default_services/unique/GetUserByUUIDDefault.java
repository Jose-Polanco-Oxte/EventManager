package jpolanco.springbootapp.user.application.default_services.unique;

import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.use_case.unique.GetUserByIdUC;
import jpolanco.springbootapp.user.domain.model.value_objects.User;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class GetUserById implements GetUserByIdUC {
    private final UserQueryRepository queryRepository;

    @Override
    public Optional<User> get(UUID userId) {
        return queryRepository.findByUuid(userId);
    }
}