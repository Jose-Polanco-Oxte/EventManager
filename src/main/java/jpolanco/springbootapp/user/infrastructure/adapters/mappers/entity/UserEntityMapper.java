package jpolanco.springbootapp.user.infrastructure.adapters.mappers.entity;

import jpolanco.springbootapp.user.domain.model.value_objects.User;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.RoleEntity;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.UserEntity;

import java.util.Set;

public interface UserEntityMapper {
    UserEntity fromDomain(User user, Set<RoleEntity> roles);
    User fromPersistence(UserEntity userEntity);
}