package jpolanco.springbootapp.user.infrastructure.adapters.mappers.entity;

import jpolanco.springbootapp.shared.infrastructure.mappers.EntityMapper;
import jpolanco.springbootapp.user.application.utils.TokenE;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.TokenEntity;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.UserEntity;

public interface TokenEntityMapper extends EntityMapper<TokenEntity, TokenE> {
    public TokenEntity toEntity(TokenE token, UserEntity user);
    public TokenE toDomain(TokenEntity entity);
}
