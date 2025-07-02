package jpolanco.springbootapp.user.infrastructure.adapters.mappers.entity;

import jpolanco.springbootapp.user.application.utils.TokenE;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.TokenEntity;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.UserEntity;

public interface TokenEntityMapper {
    TokenEntity fromDomain(TokenE token, UserEntity user);
    TokenE fromPersistence(TokenEntity entity);
}
