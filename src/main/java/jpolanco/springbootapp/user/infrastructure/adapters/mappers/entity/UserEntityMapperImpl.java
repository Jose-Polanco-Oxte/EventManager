package jpolanco.springbootapp.user.infrastructure.adapters.mappers.entity;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.model.User;

import jpolanco.springbootapp.user.domain.model.value_objects.UserStatus;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.RoleEntity;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.UserEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class UserEntityMapperImpl implements UserEntityMapper {
    public User toDomain(UserEntity userEntity) {
        Result<User> result = User.load(
                userEntity.getId().toString(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getRoles()
                        .stream()
                        .map(RoleEntity::getName)
                        .toList(),
                userEntity.getStatus(),
                userEntity.getQrFileName(),
                userEntity.getCreatedAt()
        );
        if (result.isFailure()) {
            throw new IllegalArgumentException("Data do not match : " + result.getError());
        }
        return result.getValue();
    }

    public UserEntity toEntity(User user) {
        return new UserEntity(
                UUID.fromString(user.getId()),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getEncodedPassword(),
                user.getRoles()
                        .stream()
                        .map(RoleEntity::new)
                        .collect(Collectors.toSet()),
                UserStatus.valueOf(user.getStatus()),
                user.getCreatedAt(),
                user.getQRFileName()
        );
    }
}
