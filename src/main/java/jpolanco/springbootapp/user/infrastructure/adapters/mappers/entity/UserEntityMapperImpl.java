package jpolanco.springbootapp.user.infrastructure.adapters.mappers.entity;

import jpolanco.springbootapp.user.domain.model.value_objects.User;

import jpolanco.springbootapp.user.domain.model.value_objects.UserRoles;
import jpolanco.springbootapp.user.domain.model.value_objects.UserStatus;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.RoleEntity;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.UserEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserEntityMapperImpl implements UserEntityMapper {
    public User load(UserEntity userEntity) {
        return User.fromPersisted(
                userEntity.getId(),
                userEntity.getUuid(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getRoles()
                        .stream()
                        .map(r -> UserRoles.fromString(r.getName()))
                        .collect(Collectors.toSet()),
                userEntity.getStatus(),
                userEntity.getQrFileName(),
                userEntity.getCreatedAt()
        );
    }

    public UserEntity toEntity(User user) {
        return new UserEntity(
                user.getId(),
                user.getUUID(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getEncodedPassword(),
                user.getRoles()
                        .stream()
                        .map(RoleEntity::new)
                        .collect(Collectors.toSet()),
                UserStatus.valueOf(user.getStatus().getValue()),
                user.getCreatedAt(),
                user.getQRFileName()
        );
    }
}
