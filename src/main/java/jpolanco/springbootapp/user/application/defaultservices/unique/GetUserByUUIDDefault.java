package jpolanco.springbootapp.user.application.defaultservices.unique;

import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.usecase.unique.GetUserByUUID;
import jpolanco.springbootapp.user.domain.model.valueobjects.User;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class GetUserByUUIDDefault implements GetUserByUUID {
    private final UserQueryRepository queryRepository;

    @Override
    public Optional<User> get(UUID userId) {
        return queryRepository.findByUuid(userId);
    }
}