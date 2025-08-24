package jpolanco.applicationcore.user.application.services.implementation.queries;

import jpolanco.applicationcore.user.application.mappers.UserDto;
import jpolanco.applicationcore.user.application.ports.output.UserQueryRepository;
import jpolanco.applicationcore.user.application.services.interfaces.queries.FindUserByEmail;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FindUserByEmailAdmin implements FindUserByEmail {

    private final UserQueryRepository userQueryRepository;

    @Override
    public Optional<UserResponse> find(String email) {
        return userQueryRepository.findByEmail(email)
                .map(UserDto::toResponse);
    }
}
