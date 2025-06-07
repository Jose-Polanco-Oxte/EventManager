package jpolanco.springbootapp.user.application.services;

import jpolanco.springbootapp.user.application.ports.input.QRProvider;
import jpolanco.springbootapp.user.application.ports.output.UserRepository;
import jpolanco.springbootapp.user.application.utils.UserUpdater;
import jpolanco.springbootapp.user.application.utils.UserValidation;
import jpolanco.springbootapp.user.application.uc.UpdateUserUC;
import jpolanco.springbootapp.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UpdateUser implements UpdateUserUC {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final QRProvider qrProvider;
    private final UserValidation userValidation;

    @Override
    public UserUpdater setChanges(User user) {
        return new UserUpdater(user, userRepository, passwordEncoder, qrProvider, userValidation);
    }
}