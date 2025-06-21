package jpolanco.springbootapp.user.infrastructure.adapters.output.repository;


import jpolanco.springbootapp.shared.utils.TokenStatus;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaTokenRepository extends JpaRepository<TokenEntity, Long> {
    Optional<TokenEntity> findByToken(String token);
    void deleteAllByUserId(UUID user_id);

    List<TokenEntity> findAllByUserId(UUID user_id);

    @Query("SELECT COUNT(t) FROM tokens t WHERE t.user.id = ?1 AND t.status = 'ACTIVE'")
    int countByUserIdAndStatusIsActive(UUID uuid);

    void deleteAllByStatus(TokenStatus status);

    @Modifying
    @Query("""
              UPDATE tokens t SET t.status = 'REVOKED' WHERE t.user.id = ?1
    """)
    int revokeAllByUserId(UUID userId);

    void deleteByToken(String token);
}
