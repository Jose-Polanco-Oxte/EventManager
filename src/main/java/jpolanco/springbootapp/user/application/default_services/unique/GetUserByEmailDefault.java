package jpolanco.springbootapp.user.application.default_services.unique;

import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.use_case.unique.GetUserByEmailUC;
import jpolanco.springbootapp.user.domain.model.value_objects.User;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class GetUserByEmail implements GetUserByEmailUC {
    private final UserQueryRepository queryRepository;

    @Override
    public Optional<User> get(String email) {
        return queryRepository.findByEmail(email);
    }
}
