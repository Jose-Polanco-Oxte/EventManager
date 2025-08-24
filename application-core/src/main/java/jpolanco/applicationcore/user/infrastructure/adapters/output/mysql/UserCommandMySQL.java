package jpolanco.applicationcore.user.infrastructure.adapters.output.mysql;

import jpolanco.applicationcore.user.application.ports.output.UserCommandRepository;
import jpolanco.applicationcore.user.domain.model.User;
import jpolanco.applicationcore.user.domain.repositories.UserRepository;
import jpolanco.applicationcore.user.infrastructure.adapters.output.repositories.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserCommandMySQL implements UserCommandRepository {
    private final JpaUserRepository jpaUserRepository;
    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long userId) {
        jpaUserRepository.deleteById(userId);
    }
}
