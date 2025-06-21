package jpolanco.springbootapp.user.infrastructure.adapters.output.mysql;

import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.infrastructure.adapters.mappers.entity.UserEntityMapper;
import jpolanco.springbootapp.user.infrastructure.adapters.output.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserCommandMySQL implements UserCommandRepository {
    private final JpaUserRepository jpaUserRepository;
    private final UserEntityMapper mapper;

    @Override
    public User save(User user) {
        jpaUserRepository.save(mapper.toEntity(user));
        return user;
    }

    @Override
    public void deleteById(String userId) {
        jpaUserRepository.deleteById(UUID.fromString(userId));
    }
}
