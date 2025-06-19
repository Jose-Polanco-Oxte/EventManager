package jpolanco.springbootapp.user.infrastructure.adapters.output.repository;


import jpolanco.springbootapp.shared.utils.TokenStatus;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaTokenRepository extends JpaRepository<TokenEntity, Long> {
    Optional<TokenEntity> findByToken(String token);
    void deleteAllByUserId(UUID user_id);

    List<TokenEntity> findAllByUserId(UUID user_id);

    int countByUserId(UUID userId);

    @Query("SELECT COUNT(t) FROM tokens t WHERE t.user.id = ?1 AND t.status = 'ACTIVE'")
    int countByUserIdAndStatusIsActive(UUID uuid);

    List<TokenEntity> findAllByStatusIs(TokenStatus status);

    void deleteAllByStatus(TokenStatus status);
}
