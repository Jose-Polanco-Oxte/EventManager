package jpolanco.springbootapp.user.application.services;

import jpolanco.springbootapp.user.application.errors.IllegalUserOperation;
import jpolanco.springbootapp.user.application.ports.input.QRProvider;
import jpolanco.springbootapp.user.application.ports.output.UserRepository;
import jpolanco.springbootapp.user.application.uc.UpdateProfileUC;
import jpolanco.springbootapp.user.application.utils.UserUpdater;
import jpolanco.springbootapp.user.application.utils.UserValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UpdateProfile implements UpdateProfileUC {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final QRProvider qrProvider;
    private final UserValidation userValidation;

    @Override
    public UserUpdater setChanges(String userId) {
        return new UserUpdater(userRepository.findById(userId)
                .orElseThrow(() -> new IllegalUserOperation("User not found")), userRepository, passwordEncoder, qrProvider, userValidation);
    }
}
