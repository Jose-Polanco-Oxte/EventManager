package jpolanco.springbootapp.user.infrastructure.adapters.output.mysql;

import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.domain.model.value_objects.User;
import jpolanco.springbootapp.user.infrastructure.adapters.mappers.entity.UserEntityMapper;
import jpolanco.springbootapp.user.infrastructure.adapters.output.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserCommandMySQL implements UserCommandRepository {
    private final JpaUserRepository jpaUserRepository;
    private final UserEntityMapper mapper;

    @Override
    public User save(User user) {
        var userEntity = mapper.toEntity(user);
        var savedEntity = jpaUserRepository.save(userEntity);
        return mapper.load(savedEntity);
    }

    @Override
    public void deleteById(Long userId) {
        jpaUserRepository.deleteById(userId);
    }
}
