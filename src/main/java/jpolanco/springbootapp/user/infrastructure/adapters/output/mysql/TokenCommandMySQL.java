package jpolanco.springbootapp.user.infrastructure.adapters.output.mysql;

import jpolanco.springbootapp.user.application.utils.TokenStatus;
import jpolanco.springbootapp.user.application.ports.output.JwtCommandRepository;
import jpolanco.springbootapp.user.application.utils.TokenE;
import jpolanco.springbootapp.user.infrastructure.adapters.mappers.entity.TokenEntityMapper;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.TokenEntity;
import jpolanco.springbootapp.user.infrastructure.adapters.output.repository.JpaTokenRepository;
import jpolanco.springbootapp.user.infrastructure.adapters.output.repository.JpaUserRepository;
import jpolanco.springbootapp.user.infrastructure.errors.UserIntegrity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TokenCommandMySQL implements JwtCommandRepository {
    private final JpaTokenRepository jpaTokenRepository;
    private final TokenEntityMapper tokenEntityMapper;
    private final JpaUserRepository jpaUserRepository;

    @Override
    public TokenE save(TokenE entity) {
        var userEntity = jpaUserRepository.getReferenceById(entity.getUserId());
        var tokenEntity = tokenEntityMapper.fromDomain(entity, userEntity);
        TokenEntity savedToken = jpaTokenRepository.save(tokenEntity);
        return tokenEntityMapper.fromPersistence(savedToken);
    }

    @Override
    public void deleteById(String s) {
        jpaTokenRepository.deleteByToken(s);
    }

    @Override
    public void deleteAllByUserId(Long userId) {
        jpaTokenRepository.deleteAllByUserId(userId);
    }

    @Override
    public void revokeByToken(String token) {
        var maybeToken = jpaTokenRepository.findByToken(token);
        if (maybeToken.isPresent()) {
            var tokenEntity = maybeToken.get();
            tokenEntity.setStatus(TokenStatus.REVOKED);
            jpaTokenRepository.save(tokenEntity);
        } else {
            throw new UserIntegrity("Token not found");
        }
    }

    @Override
    public void saveAll(List<TokenE> tokens) {
        Set<TokenEntity> tokenEntities = tokens.stream()
                .map(t -> {
                    var userEntity = jpaUserRepository.findById(t.getUserId())
                            .orElseThrow(() -> new UserIntegrity("User not found for ID: " + t.getUserId()));
                    return tokenEntityMapper.fromDomain(t, userEntity);
                })
                .collect(Collectors.toSet());
        jpaTokenRepository.saveAll(tokenEntities);
    }

    @Override
    public void deleteAllByStatus(TokenStatus status) {
        jpaTokenRepository.deleteAllByStatus(status);
    }

    @Override
    public void revokeAllByUserId(Long userId) {
        int updatedCount = jpaTokenRepository.revokeAllByUserId(userId);
        if (updatedCount == 0) {
            throw new UserIntegrity("No tokens found for userId ID: " + userId);
        }
    }
}
