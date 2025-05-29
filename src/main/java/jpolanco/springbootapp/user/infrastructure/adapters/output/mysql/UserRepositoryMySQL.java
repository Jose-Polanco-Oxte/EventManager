package jpolanco.springbootapp.user.infrastructure.adapters.output.mysql;

import jpolanco.springbootapp.user.application.ports.output.UserRepository;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.infrastructure.adapters.mappers.entity.UserEntityMapper;
import jpolanco.springbootapp.user.infrastructure.adapters.output.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class UserRepositoryMySQL implements UserRepository {

    private final JpaUserRepository jpaUserRepository;
    private final UserEntityMapper mapper;

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findById(String userId) {
        return jpaUserRepository.findById(UUID.fromString(userId))
                .map(mapper::toDomain);
    }

    @Override
    public User save(User user) {
        jpaUserRepository.save(mapper.toEntity(user));
        return user;
    }

    @Override
    public void deleteById(String userId) {
        jpaUserRepository.deleteById(UUID.fromString(userId));
    }

    @Override
    public List<User> findAll() {
        return jpaUserRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
