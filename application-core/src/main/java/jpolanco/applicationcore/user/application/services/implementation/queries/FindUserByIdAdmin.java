package jpolanco.applicationcore.user.application.services.implementation.queries;

import jpolanco.applicationcore.user.application.mappers.UserDto;
import jpolanco.applicationcore.user.application.ports.output.UserQueryRepository;
import jpolanco.applicationcore.user.application.services.interfaces.queries.FindUserById;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindUserByIdAdmin implements FindUserById {

    private final UserQueryRepository userQueryRepository;

    @Override
    public Optional<UserResponse> find(UUID userId) {
        return userQueryRepository.findByUuid(userId)
                .map(UserDto::toResponse);
    }
}
