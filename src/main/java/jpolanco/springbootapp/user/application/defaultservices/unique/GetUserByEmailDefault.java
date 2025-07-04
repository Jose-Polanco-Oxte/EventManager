package jpolanco.springbootapp.user.application.defaultservices.unique;

import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.usecase.unique.GetUserByEmail;
import jpolanco.springbootapp.user.domain.model.valueobjects.User;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class GetUserByEmailDefault implements GetUserByEmail {
    private final UserQueryRepository queryRepository;

    @Override
    public Optional<User> get(String email) {
        return queryRepository.findByEmail(email);
    }
}
