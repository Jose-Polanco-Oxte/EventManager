package jpolanco.springbootapp.user.infrastructure.adapters.mappers.entity;

import jpolanco.springbootapp.shared.infrastructure.EntityMapper;
import jpolanco.springbootapp.user.domain.model.Token;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.TokenEntity;

public interface TokenEntityMapper extends EntityMapper<TokenEntity, Token> {
}
