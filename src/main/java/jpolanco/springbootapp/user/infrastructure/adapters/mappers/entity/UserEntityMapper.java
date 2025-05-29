package jpolanco.springbootapp.user.infrastructure.adapters.mappers.entity;

import jpolanco.springbootapp.shared.infrastructure.EntityMapper;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.UserEntity;

public interface UserEntityMapper extends EntityMapper<UserEntity, User> {
}